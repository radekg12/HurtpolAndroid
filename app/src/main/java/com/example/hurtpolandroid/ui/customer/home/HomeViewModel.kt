package com.example.hurtpolandroid.ui.customer.home

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.hurtpolandroid.ui.model.CustomerDTO
import com.example.hurtpolandroid.ui.model.ProductPage
import com.example.hurtpolandroid.ui.service.CustomerService
import com.example.hurtpolandroid.ui.service.ProductService
import com.example.hurtpolandroid.ui.utils.HurtpolServiceGenerator
import retrofit2.Call

class HomeViewModel(context: Context) : ViewModel() {
    private var productService = HurtpolServiceGenerator().createService(ProductService::class.java)
    private var customerService = HurtpolServiceGenerator().createServiceWithToken(CustomerService::class.java, context)

    fun getUser(): Call<CustomerDTO> {
        return customerService.getUser()
    }

    fun getProducts(currentPageNumber: Int): Call<ProductPage> {
        return productService.getProducts(currentPageNumber, 5)
    }
}