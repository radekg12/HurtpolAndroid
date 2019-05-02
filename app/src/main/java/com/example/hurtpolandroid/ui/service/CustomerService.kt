package com.example.hurtpolandroid.ui.service

import com.example.hurtpolandroid.ui.model.CustomerDTO
import retrofit2.Call
import retrofit2.http.GET

interface CustomerService {
    @GET("customer")
    fun getUser(): Call<CustomerDTO>
}