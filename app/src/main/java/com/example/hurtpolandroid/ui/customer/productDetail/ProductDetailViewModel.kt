package com.example.hurtpolandroid.ui.customer.productDetail

import androidx.lifecycle.ViewModel
import com.example.hurtpolandroid.ui.model.Product
import com.example.hurtpolandroid.ui.service.ProductService
import com.example.hurtpolandroid.ui.utils.HurtpolServiceGenerator
import retrofit2.Call

class ProductDetailViewModel : ViewModel() {
    private var productService = HurtpolServiceGenerator().createService(ProductService::class.java)

    fun getProductDetail(productID: Int): Call<Product> {
        return productService.getProductByID(productID)
    }
}