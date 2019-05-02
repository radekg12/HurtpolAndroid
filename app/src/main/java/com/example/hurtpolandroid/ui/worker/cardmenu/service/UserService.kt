package com.example.hurtpolandroid.ui.worker.cardmenu.service

import com.example.hurtpolandroid.ui.worker.cardmenu.model.CustomerDTO
import retrofit2.Call
import retrofit2.http.GET

interface UserService {
    @GET("customer")
    fun getUser(): Call<CustomerDTO>
}