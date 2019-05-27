package com.example.hurtpolandroid.ui.customer.shoppingCart

import android.app.Activity
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import org.json.JSONArray
import org.json.JSONObject

object PaymentsUtil {

    private val baseRequest = JSONObject().apply {
        put("apiVersion", 2)
        put("apiVersionMinor", 0)
    }

    private fun gatewayTokenizationSpecification(): JSONObject {
        return JSONObject().apply {
            put("type", "PAYMENT_GATEWAY")
            put("parameters", JSONObject(PaymentConstants.PAYMENT_GATEWAY_TOKENIZATION_PARAMETERS))
        }
    }

    private val allowedCardNetworks = JSONArray(PaymentConstants.SUPPORTED_NETWORKS)

    private val allowedCardAuthMethods = JSONArray(PaymentConstants.SUPPORTED_METHODS)

    private fun baseCardPaymentMethod(): JSONObject {
        return JSONObject().apply {

            val parameters = JSONObject().apply {
                put("allowedAuthMethods", allowedCardAuthMethods)
                put("allowedCardNetworks", allowedCardNetworks)
                put("billingAddressRequired", true)
                put("billingAddressParameters", JSONObject().apply {
                    put("format", "FULL")
                })
            }

            put("type", "CARD")
            put("parameters", parameters)
        }
    }

    private fun cardPaymentMethod(): JSONObject {
        val cardPaymentMethod = baseCardPaymentMethod()
        cardPaymentMethod.put("tokenizationSpecification", gatewayTokenizationSpecification())
        return cardPaymentMethod
    }

    fun isReadyToPayRequest(): JSONObject {
        val isReadyToPayRequest = JSONObject(baseRequest.toString())
        isReadyToPayRequest.put(
            "allowedPaymentMethods", JSONArray().put(baseCardPaymentMethod())
        )
        return isReadyToPayRequest

    }

    private val merchantInfo: JSONObject
        get() = JSONObject().put("merchantName", "Hurtpol")

    fun createPaymentsClient(activity: Activity): PaymentsClient {
        val walletOptions = Wallet.WalletOptions.Builder()
            .setEnvironment(PaymentConstants.PAYMENTS_ENVIRONMENT)
            .build()

        return Wallet.getPaymentsClient(activity, walletOptions)
    }

    private fun getTransactionInfo(price: String): JSONObject {
        return JSONObject().apply {
            put("totalPrice", price)
            put("totalPriceStatus", "FINAL")
            put("currencyCode", PaymentConstants.CURRENCY_CODE)
        }
    }

    fun getPaymentDataRequest(price: String): JSONObject {
        return JSONObject(baseRequest.toString()).apply {
            put("allowedPaymentMethods", JSONArray().put(cardPaymentMethod()))
            put("transactionInfo", getTransactionInfo(price))
            put("merchantInfo", merchantInfo)
            put("emailRequired", true)

            val shippingAddressParameters = JSONObject().apply {
                put("phoneNumberRequired", false)
                put("allowedCountryCodes", JSONArray(PaymentConstants.SHIPPING_SUPPORTED_COUNTRIES))
            }
            put("shippingAddressRequired", true)
            put("shippingAddressParameters", shippingAddressParameters)
        }
    }
}