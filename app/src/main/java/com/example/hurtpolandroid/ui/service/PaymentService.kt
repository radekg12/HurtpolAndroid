package com.example.hurtpolandroid.ui.service

import com.example.hurtpolandroid.ui.model.PaymentRequest
import com.example.hurtpolandroid.ui.model.PaymentResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentService {
    @POST("payu/make/payment/googlepay")
    fun payForOrder(@Body payment: PaymentRequest): Call<PaymentResponse>
}