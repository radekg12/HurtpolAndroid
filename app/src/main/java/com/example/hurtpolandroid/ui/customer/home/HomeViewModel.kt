package com.example.hurtpolandroid.ui.customer.home

import androidx.lifecycle.ViewModel
import com.example.hurtpolandroid.ui.model.Content
import com.example.hurtpolandroid.ui.model.Product
import com.example.hurtpolandroid.ui.service.ProductService
import com.example.hurtpolandroid.ui.utils.HurtpolServiceGenerator
import retrofit2.Call

class HomeViewModel : ViewModel() {
    private var productService = HurtpolServiceGenerator().createService(ProductService::class.java)

    fun getProducts(currentPageNumber: Int): Call<Content<Product>> {
        return productService.getProducts(currentPageNumber)
    }
}