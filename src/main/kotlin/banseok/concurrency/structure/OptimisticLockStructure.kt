package banseok.concurrency.structure

import banseok.concurrency.service.OptimisticLockItemService
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.stereotype.Component

@Component
class OptimisticLockStructure(private val optimisticLockItemService: OptimisticLockItemService) {

    fun optimisticLockBuyItem(id: Long, quantity: Long) {
        var retryCount = 0
        val maxRetry = 1000
        while (retryCount < maxRetry) {
            try {
                optimisticLockItemService.buyItem(id, quantity)
            } catch (ex: ObjectOptimisticLockingFailureException) {
                // 예외 처리: 버전 충돌 시 재시도
                retryCount++
                Thread.sleep(100)
            }
        }
    }
}