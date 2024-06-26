package banseok.concurrency.service;

import banseok.concurrency.exception.ItemNotExistException
import banseok.concurrency.repository.ItemRepository
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
class NamedLockItemService(private val itemRepository: ItemRepository){

    // 부모 트랜잭션과 분리
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun buyItem(id:Long, quantity:Long) {
        val item = itemRepository.findWithPessimisticLockById(id)
            ?: throw ItemNotExistException("아이템이 존재하지 않습니다")
        item.decreaseStock(quantity);
    }
}
