package com.example.hurtpolandroid.ui.model


data class PaymentRequest(
    val address: Address,
    val googlePayToken: String
)

data class Address(
    val city: String,
    val postcode: String,
    val street: String
)

data class PaymentResponse(
    val orderId: String,
    val redirectUri: String
)