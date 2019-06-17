package com.example.hurtpolandroid.service

import com.example.hurtpolandroid.data.model.PaymentRequest
import com.example.hurtpolandroid.data.model.PaymentResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentService {
    @POST("payment/googlepay")
    fun payForOrder(@Body payment: PaymentRequest): Call<PaymentResponse>
}