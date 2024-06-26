package banseok.concurrency.service

import banseok.concurrency.domain.Item
import banseok.concurrency.exception.ItemNotExistException
import banseok.concurrency.repository.ItemRepository
import mu.KotlinLogging
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@SpringBootTest
class PessimisticLockItemServiceTest {

    private val logger = KotlinLogging.logger {}


    @Autowired
    private lateinit var pessimisticLockItemService: PessimisticLockItemService

    @Autowired
    private lateinit var itemRepository: ItemRepository

    @BeforeEach
    fun before() {
        val item = Item("아이템명", 1000, "이미지", 100L)
        itemRepository.save(item)
        logger.info { "아이템 저장" }
    }

    @AfterEach
    fun after() {
        logger.info { "아이템 삭제" }
        itemRepository.deleteAll()
    }

    @Test
    @DisplayName("비관적 락 적용 - 동시에 100개의 아이템 구매 요청 테스트")
    fun buyItem_PessimisticLock_Test() {
        val threadCount = 100
        val executorService: ExecutorService = Executors.newFixedThreadPool(32)
        val countDownLatch = CountDownLatch(threadCount)

        repeat(threadCount) {
            executorService.submit {
                try {
                    pessimisticLockItemService.buyItem(1L, 1L)
                } finally {
                    countDownLatch.countDown()
                }
            }
        }

        countDownLatch.await()

        val item = itemRepository.findById(1L)
            .orElseThrow {ItemNotExistException("아이템이 존재하지 않습니다") }
        Assertions.assertThat(item.stock).isEqualTo(0L)
    }
}