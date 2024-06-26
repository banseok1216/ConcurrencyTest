package banseok.concurrency.service;

import banseok.concurrency.domain.Item
import banseok.concurrency.exception.ItemNotExistException
import banseok.concurrency.repository.ItemRepository
import banseok.concurrency.structure.NamedLockStructure
import lombok.extern.slf4j.Slf4j
import mu.KotlinLogging
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
@Slf4j
class NamedLockItemServiceTest {

    private val logger = KotlinLogging.logger {}

    @Autowired
    private lateinit var namedLockStructure: NamedLockStructure

    @Autowired
    private lateinit var itemRepository: ItemRepository

    @BeforeEach
    fun before() {
        val item =  Item("아이템명", 1000, "이미지", 100L)
        itemRepository.save(item)
        logger.info("아이템 저장")
    }

    @AfterEach
    fun after() {
        logger.info("아이템 삭제")
        itemRepository.deleteAll()
    }

    @Test
    @DisplayName("네임드 락 적용 - 동시에 100개의 아이템 구매 요청 테스트")
    @Throws(InterruptedException::class)
    fun buyItem_NamedLock_Test() {
        val threadCount = 100

        val executorService = Executors.newFixedThreadPool(32)
        val countDownLatch = CountDownLatch(threadCount)

        for (i in 0 until threadCount) {
            executorService.submit {
                try {
                    namedLockStructure.namedLockBuyItem(1L, 3000, 1L)
                } finally {
                    countDownLatch.countDown()
                }
            }
        }

        countDownLatch.await()

        val item: Item = itemRepository.findById(1L)
            .orElseThrow { ItemNotExistException("아이템이 존재하지 않습니다") }
        Assertions.assertThat(item.stock).isEqualTo(0L)
    }
}
