package com.example.hurtpolandroid.service

import com.example.hurtpolandroid.data.model.Product
import com.example.hurtpolandroid.data.model.ProductPage
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductService {

    @GET("products")
    fun getProducts(@Query("page") pageNumber: Int, @Query("size") pageSize: Int): Call<ProductPage>

    @GET("products/{id}/detail")
    fun getProductById(@Path("id") id: Int): Call<Product>
}