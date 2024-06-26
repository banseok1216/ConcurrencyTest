package banseok.concurrency.structure;

import banseok.concurrency.repository.LockRepository
import banseok.concurrency.service.NamedLockItemService
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class NamedLockStructure(private val lockRepository: LockRepository, private val namedLockItemService: NamedLockItemService) {

    @Transactional
    fun namedLockBuyItem(id: Long, timeoutSeconds: Int, quantity: Long) {
        try {
            // NamedLock 획득
            lockRepository.getLock(id.toString(), timeoutSeconds)
            namedLockItemService.buyItem(id, quantity)
        } finally {
            // NamedLock 해제
            lockRepository.releaseLock(id.toString())
        }
    }
}
