package com.example.hurtpolandroid.ui.customer.shoppingCart

import com.google.android.gms.wallet.WalletConstants

object PaymentConstants {

    const val PAYMENTS_ENVIRONMENT = WalletConstants.ENVIRONMENT_TEST

    val SUPPORTED_NETWORKS = listOf(
        "MASTERCARD",
        "VISA"
    )

    val SUPPORTED_METHODS = listOf(
        "PAN_ONLY",
        "CRYPTOGRAM_3DS"
    )

    const val CURRENCY_CODE = "PLN"

    val SHIPPING_SUPPORTED_COUNTRIES = listOf("PL", "DE")

    private const val PAYMENT_GATEWAY_TOKENIZATION_NAME = "payu"
    private const val GATEWAY_MERCHANT_ID = "351489"

    val PAYMENT_GATEWAY_TOKENIZATION_PARAMETERS = mapOf(
        "gateway" to PAYMENT_GATEWAY_TOKENIZATION_NAME,
        "gatewayMerchantId" to GATEWAY_MERCHANT_ID
    )
}