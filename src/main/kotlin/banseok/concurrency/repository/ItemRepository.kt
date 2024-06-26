package banseok.concurrency.repository

import banseok.concurrency.domain.Item
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


interface ItemRepository : JpaRepository<Item, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findWithPessimisticLockById(@Param("id") id: Long): Item?

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select i from Item i where i.id = :id")
    fun findByIdWithOptimisticLockById(id: Long): Item?
}