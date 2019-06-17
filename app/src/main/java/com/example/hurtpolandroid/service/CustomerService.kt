package com.example.hurtpolandroid.service

import com.example.hurtpolandroid.data.model.Customer
import retrofit2.Call
import retrofit2.http.GET

interface CustomerService {
    @GET("account/my-data")
    fun getUser(): Call<Customer>
}