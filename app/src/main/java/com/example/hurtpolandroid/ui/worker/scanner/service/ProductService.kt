package com.example.hurtpolandroid.ui.worker.scanner.service

import com.example.hurtpolandroid.ui.worker.scanner.Product
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductService {
    @POST("products/warehouse/take/{id}")
    fun take(@Path("id") id: Long, @Query("quantity") quantity: Int): Call<Product>

    @POST("products/warehouse/take/{id}")
    fun put(@Path("id") id: Long, @Query("quantity") quantity: Int): Call<Product>
}