package banseok.concurrency.service;

import banseok.concurrency.exception.ItemNotExistException
import banseok.concurrency.repository.ItemRepository
import org.springframework.stereotype.Service

@Service
class SynchronizedItemService(private val itemRepository: ItemRepository) {

    @Synchronized
    fun buyItem(id: Long, quantity: Long) {
        val item = itemRepository.findById(id)
            .orElseThrow { ItemNotExistException("아이템이 존재하지 않습니다") }
        item.decreaseStock(quantity)
        itemRepository.save(item)
    }
}
