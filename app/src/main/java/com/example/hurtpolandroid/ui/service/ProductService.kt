package com.example.hurtpolandroid.ui.service

import com.example.hurtpolandroid.ui.model.Content
import com.example.hurtpolandroid.ui.model.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {

    @GET("/products")
    fun getProducts(): Call<Content<Product>>

    @GET("/products/detail/{id}")
    fun getProductByID(@Path("id") id: Int): Call<Product>
}