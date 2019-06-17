package com.example.hurtpolandroid.service

import com.example.hurtpolandroid.data.model.Account
import com.example.hurtpolandroid.data.model.Customer
import com.example.hurtpolandroid.data.model.SigninResponse
import com.example.hurtpolandroid.data.model.UserDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationService {
    @POST("auth/login")
    fun signin(@Body userDTO: UserDTO): Call<SigninResponse>

    @POST("auth/register")
    fun signup(@Body account: Account): Call<Customer>

}