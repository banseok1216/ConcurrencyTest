package banseok.concurrency.repository;

import banseok.concurrency.domain.Item
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface LockRepository: JpaRepository<Item, Long> {
    @Query(value = "select GET_LOCK(:key, :timeoutSeconds)", nativeQuery = true)
    fun getLock(key: String?, timeoutSeconds: Int)
    @Query(value = "select RELEASE_LOCK(:key)", nativeQuery = true)
    fun releaseLock(key: String?)
}
