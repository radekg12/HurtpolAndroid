package com.example.hurtpolandroid.ui.service

import com.example.hurtpolandroid.ui.model.Product
import com.example.hurtpolandroid.ui.model.ProductPage
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductService {

    @GET("products")
    fun getProducts(@Query("page") pageNumber: Int, @Query("size") pageSize: Int): Call<ProductPage>

    @GET("products/detail/{id}")
    fun getProductByID(@Path("id") id: Int): Call<Product>
}