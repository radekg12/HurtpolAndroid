package com.example.hurtpolandroid.ui.signin.domain.service

import com.example.hurtpolandroid.ui.signin.domain.model.SigninResponse
import com.example.hurtpolandroid.ui.signin.domain.model.UserDTO
import com.example.hurtpolandroid.ui.signup.domain.model.Account
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthenticationService {
    @Headers("Content-Type:application/json")
    @POST("auth/signin")
    fun signin(@Body userDTO: UserDTO): Call<SigninResponse>

    @POST("auth/signup")
    fun signup(@Body account: Account): Call<Boolean>

}