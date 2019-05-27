package com.example.hurtpolandroid.ui.customer.shoppingCart

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hurtpolandroid.ui.model.*
import com.example.hurtpolandroid.ui.service.PaymentService
import com.example.hurtpolandroid.ui.service.ShoppingCartService
import com.example.hurtpolandroid.ui.utils.HurtpolServiceGenerator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat

class ShoppingCartViewModel(val context: Context) : ViewModel(), ShoppingCartAdapter.OnItemClickListener {

    private var shoppingCartService =
        HurtpolServiceGenerator().createServiceWithToken(ShoppingCartService::class.java, context)

    private var paymentService =
        HurtpolServiceGenerator().createServiceWithToken(PaymentService::class.java, context)

    var shoppingCart = MutableLiveData<ArrayList<ShoppingCartItem>>()

    fun getShoppingCart() {
        shoppingCartService.getShoppingCart().enqueue(object : Callback<ArrayList<ShoppingCartItem>> {
            override fun onFailure(call: Call<ArrayList<ShoppingCartItem>>, t: Throwable) {
                println("Blad$t")
            }

            override fun onResponse(
                call: Call<ArrayList<ShoppingCartItem>>,
                response: Response<ArrayList<ShoppingCartItem>>
            ) {
                shoppingCart.value = response.body()
            }

        })
    }

    fun getTotalItem(): Int {
        return shoppingCart.value?.map { it.quantity }?.sum()!!
    }

    fun getTotalPriceWithCurrency(): String {
        val format = NumberFormat.getCurrencyInstance()
        return format.format(getTotalPrice())
    }

    fun getTotalPrice(): Double {
        return shoppingCart.value!!.sumByDouble { it.quantity * (it.product.unitPrice.div(100)).toDouble() }
    }

    fun removeProduct(position: Int) {
        shoppingCartService.removeProduct(shoppingCart.value!![position].product.id)
            .enqueue(object : Callback<ShoppingCartItem> {
                override fun onFailure(call: Call<ShoppingCartItem>, t: Throwable) {
                    println("1")
                }

                override fun onResponse(call: Call<ShoppingCartItem>, response: Response<ShoppingCartItem>) {
                    val list: ArrayList<ShoppingCartItem> = shoppingCart.value!!
                    list.removeAt(position)
                    shoppingCart.value = list
                    toastOnDelete()
                }

            })
    }

    private fun updateProductCount(cart: ShoppingCartItem, number: Int) {
        shoppingCartService.updateProduct(ShoppingCartItemToUpdate(cart.product.id, cart.quantity + number))
            .enqueue(object : Callback<ShoppingCartItem> {
                override fun onFailure(call: Call<ShoppingCartItem>, t: Throwable) {
                    Toast.makeText(context, "Blad", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<ShoppingCartItem>, response: Response<ShoppingCartItem>) {
                    val list: ArrayList<ShoppingCartItem> = shoppingCart.value!!
                    list[list.indexOf(cart)].quantity = response.body()!!.quantity
                    shoppingCart.value = list
                }

            })
    }

    private fun plusProduct(item: ShoppingCartItem) {
        updateProductCount(item, 1)
    }

    override fun onBtnMinusClick(item: ShoppingCartItem) {
        updateProductCount(item, -1)
    }

    override fun onBtnPlusClick(item: ShoppingCartItem) {
        plusProduct(item)
    }

    fun toastOnDelete() {
        Toast.makeText(context, "Usunięto produkt z koszyka", Toast.LENGTH_LONG).show()
    }

    fun payForOrder(deliveryAddress: Address, googlePaymentToken: String) {
        paymentService.payForOrder(PaymentRequest(deliveryAddress, googlePaymentToken))
            .enqueue(object : Callback<PaymentResponse> {
                override fun onFailure(call: Call<PaymentResponse>, t: Throwable) {
                    Toast.makeText(context, "Blad", Toast.LENGTH_LONG).show()
                    Log.d("błąd", t.message)
                }

                override fun onResponse(call: Call<PaymentResponse>, response: Response<PaymentResponse>) {
                    Toast.makeText(context, "Udana transakcja", Toast.LENGTH_LONG).show()
                    Log.d("success", response.message())
                }
            })
    }

}