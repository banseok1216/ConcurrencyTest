package banseok.concurrency.structure

import banseok.concurrency.service.ItemService
import mu.KotlinLogging
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class RedissonStructure(private val redissonClient: RedissonClient, private val itemService: ItemService) {
    private val log = KotlinLogging.logger {}
    fun buyItem(key: Long, quantity: Long) {
        val lock = redissonClient.getLock(key.toString())
        try {
            val available = lock.tryLock(10, 2, TimeUnit.SECONDS)
            if (!available) {
                log.info { "lock 획득 실패" }
                return
            }
            itemService.buyItem(key, quantity)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        } finally {
            lock.unlock()
        }
    }
}
