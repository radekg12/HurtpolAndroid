package com.example.hurtpolandroid.service

import com.example.hurtpolandroid.data.model.Product
import com.example.hurtpolandroid.data.model.WarehouseStateUpdate
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT

interface WarehouseService {
    @PUT("warehouse/delivery")
    fun take(@Body warehouseStateUpdate: WarehouseStateUpdate): Call<Product>

    @PUT("warehouse/pick-up")
    fun put(@Body warehouseStateUpdate: WarehouseStateUpdate): Call<Product>
}