package com.example.hurtpolandroid.ui.customer.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.hurtpolandroid.data.ProductRepository
import com.example.hurtpolandroid.data.model.Product
import com.example.hurtpolandroid.data.model.Specification
import com.example.hurtpolandroid.service.ShoppingCartService
import com.example.hurtpolandroid.utils.HurtpolServiceGenerator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailViewModel(
    context: Context,
    private val productRepository: ProductRepository,
    private val productId: Int
) : ViewModel() {

    var shoppingCartResponse: MediatorLiveData<Product> = MediatorLiveData()
    private var shoppingCartService =
        HurtpolServiceGenerator().createServiceWithToken(ShoppingCartService::class.java, context)

    fun getProductDetail(): LiveData<Product> {
        return productRepository.getProduct(productId)
    }

    fun getProductSpecification(): LiveData<List<Specification>> {
        return productRepository.getSpecification(productId)
    }

    fun addProductToShoppingCart(productId: Int) {
        shoppingCartService.addShoppingCardItem(productId).enqueue(object : Callback<Product> {
            override fun onFailure(call: Call<Product>, t: Throwable) {

            }

            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                shoppingCartResponse.value = response.body()
            }

        })
    }
}