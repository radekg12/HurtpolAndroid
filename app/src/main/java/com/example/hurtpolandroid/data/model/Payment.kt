package com.example.hurtpolandroid.data.model


data class PaymentRequest(
    val address: Address,
    val googlePaymentTokenBase64: String
)

data class Address(
    val city: String,
    val postcode: String,
    val street: String
)

data class PaymentResponse(
    val orderId: String,
    val redirectUri: String,
    val status: PaymentStatus
)

data class PaymentStatus(
    val statusCode: String,
    val statusDesc: String
)