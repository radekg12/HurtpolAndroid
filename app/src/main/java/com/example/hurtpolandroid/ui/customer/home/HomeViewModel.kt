package com.example.hurtpolandroid.ui.customer.home

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.hurtpolandroid.data.model.Customer
import com.example.hurtpolandroid.data.model.Product
import com.example.hurtpolandroid.data.model.ProductPage
import com.example.hurtpolandroid.service.CustomerService
import com.example.hurtpolandroid.service.ProductService
import com.example.hurtpolandroid.utils.HurtpolServiceGenerator
import retrofit2.Call

class HomeViewModel(context: Context) : ViewModel() {
    var productList = ArrayList<Product>()
    lateinit var currentPage: ProductPage
    private var productService = HurtpolServiceGenerator().createService(ProductService::class.java)
    private var customerService = HurtpolServiceGenerator().createServiceWithToken(CustomerService::class.java, context)

    fun getUser(): Call<Customer> {
        return customerService.getUser()
    }

    fun getProducts(currentPageNumber: Int): Call<ProductPage> {
        return productService.getProducts(currentPageNumber, 5)
    }

}