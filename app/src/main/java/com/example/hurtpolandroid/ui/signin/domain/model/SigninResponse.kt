package com.example.hurtpolandroid.ui.signin.domain.model

data class SigninResponse(
    val accessToken: String,
    val tokenType: String,
    val rememberMe: Boolean,
    val authorities: List<Authority>
)