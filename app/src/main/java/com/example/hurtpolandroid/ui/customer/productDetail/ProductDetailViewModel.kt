package com.example.hurtpolandroid.ui.customer.productDetail

import android.content.Context
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.hurtpolandroid.ui.model.Product
import com.example.hurtpolandroid.ui.service.ProductService
import com.example.hurtpolandroid.ui.service.ShoppingCartService
import com.example.hurtpolandroid.ui.utils.HurtpolServiceGenerator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailViewModel(context: Context) : ViewModel() {
    var shoppingCartResponse: MediatorLiveData<Product> = MediatorLiveData()
    private var shoppingCartService = HurtpolServiceGenerator().createServiceWithToken(ShoppingCartService::class.java, context)
    private var productService = HurtpolServiceGenerator().createService(ProductService::class.java)

    fun getProductDetail(productID: Int): Call<Product> {
        return productService.getProductByID(productID)
    }

    fun addProductToShoppingCart(productId: Int){
        shoppingCartService.addProduct(productId).enqueue(object : Callback<Product> {
            override fun onFailure(call: Call<Product>, t: Throwable) {

            }

            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                shoppingCartResponse.value = response.body()
            }

        })
    }
}