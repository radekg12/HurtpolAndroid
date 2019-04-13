package com.example.hurtpolandroid.ui.signin.domain.service

import com.example.hurtpolandroid.ui.signin.domain.model.SigninResponse
import com.example.hurtpolandroid.ui.signin.domain.model.UserDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthenticationService {
    @Headers("Content-Type:application/json")
    @POST("signin")
    fun signin(@Body userDTO: UserDTO): Call<SigninResponse>

    @POST("signup")
    fun signup(@Body number: Int): Call<SigninResponse>

}