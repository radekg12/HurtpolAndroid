package com.example.hurtpolandroid.data.model

data class SigninResponse(
    val accessToken: String,
    val tokenType: String,
    val rememberMe: Boolean,
    val authorities: List<Authority>
)