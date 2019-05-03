package com.example.hurtpolandroid.ui.service

import com.example.hurtpolandroid.ui.model.Account
import com.example.hurtpolandroid.ui.model.CustomerDTO
import com.example.hurtpolandroid.ui.model.SigninResponse
import com.example.hurtpolandroid.ui.model.UserDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthenticationService {
    @Headers("Content-Type:application/json")
    @POST("auth/signin")
    fun signin(@Body userDTO: UserDTO): Call<SigninResponse>

    @POST("auth/signup")
    fun signup(@Body account: Account): Call<CustomerDTO>

}