package com.example.hurtpolandroid.ui.model

import androidx.lifecycle.LiveData
import com.example.hurtpolandroid.ui.service.ProductService
import com.example.hurtpolandroid.ui.utils.HurtpolServiceGenerator
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val executor: Executor,
    private val productDao: ProductDao
) {

    val productService = HurtpolServiceGenerator().createService(ProductService::class.java)

    fun getProduct(productId: Int): LiveData<Product> {
        refreshProduct(productId)
        return productDao.load(productId)
    }

    fun addProduct(product: Product) {
        productDao.insert(product)
    }

    fun hasProduct(productId: Int): Boolean {
        return productDao.hasProduct(productId) > 0
    }

    private fun refreshProduct(productId: Int) {
        executor.execute {
            val productExists = productDao.hasProduct(productId)
            if (productExists == 0) {
                val response = productService.getProductByID(productId).execute()
                productDao.save(response.body()!!)
            }
        }
    }
}