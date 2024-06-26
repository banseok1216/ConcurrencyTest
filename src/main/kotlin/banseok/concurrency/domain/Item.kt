package banseok.concurrency.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class Item(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var name: String? = null,

    var price: Int = 0,

    var imageUrl: String? = null,

    var stock: Long = 0
) {

    constructor(id: Long) : this() {
        this.id = id
    }

    constructor(name: String, price: Int, imageUrl: String, stock: Long) : this() {
        this.name = name
        this.price = price
        this.imageUrl = imageUrl
        this.stock = stock
    }

    fun decreaseStock(quantity: Long): Item {
        if (this.stock - quantity < 0) {
            throw IllegalArgumentException("재고가 부족합니다")
        }

        this.stock -= quantity
        return this
    }

    fun getStockQuantity(): Long {
        return stock
    }
}