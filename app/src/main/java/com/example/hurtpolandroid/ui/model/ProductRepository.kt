package com.example.hurtpolandroid.ui.model

import androidx.lifecycle.LiveData
import com.example.hurtpolandroid.ui.service.ProductService
import com.example.hurtpolandroid.ui.utils.HurtpolServiceGenerator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val productDao: ProductDao
) {
    val logger: Logger = Logger.getLogger(ProductRepository::class.java.name)
    val productService = HurtpolServiceGenerator().createService(ProductService::class.java)

    fun getProduct(productId: Int): LiveData<Product> {
        refreshProduct(productId)
        return productDao.load(productId)
    }

    fun getSpecification(productId: Int): LiveData<List<Specification>> {
        refreshProduct(productId)
        return productDao.getProductSpecification(productId)
    }

    private fun refreshProduct(productId: Int) {
        val productExists = productDao.hasProduct(productId)
        logger.info("Product exists $productExists")
        if (productExists == 0) {
            productService.getProductByID(productId).enqueue(object : Callback<Product> {
                override fun onFailure(call: Call<Product>, t: Throwable) {
                    throw t
                }

                override fun onResponse(call: Call<Product>, response: Response<Product>) {
                    productDao.insertProductWithSpecification(response.body()!!)
                }
            })
        }
    }

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: ProductRepository? = null

        fun getInstance(productDao: ProductDao) =
            instance ?: synchronized(this) {
                instance ?: ProductRepository(productDao).also { instance = it }
            }
    }
}