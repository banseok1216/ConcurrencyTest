package banseok.concurrency.service;

import banseok.concurrency.domain.Item
import banseok.concurrency.exception.ItemNotExistException
import banseok.concurrency.repository.ItemRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
class ItemService(private val itemRepository: ItemRepository) {
    @Transactional
    fun buyItem(id: Long, quantity: Long) {
        val item: Item = itemRepository.findById(id)
            .orElseThrow { ItemNotExistException("아이템이 존재하지 않습니다") }
        item.decreaseStock(quantity)
    }
}