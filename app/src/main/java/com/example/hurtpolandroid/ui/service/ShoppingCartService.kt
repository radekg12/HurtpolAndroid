package com.example.hurtpolandroid.ui.service

import com.example.hurtpolandroid.ui.model.Product
import com.example.hurtpolandroid.ui.model.ShoppingCart
import com.example.hurtpolandroid.ui.model.ShoppingCartItem
import com.example.hurtpolandroid.ui.model.ShoppingCartItemToUpdate
import retrofit2.Call
import retrofit2.http.*

interface ShoppingCartService {
    @GET("/shoppingCart")
    fun getShoppingCart(): Call<ArrayList<ShoppingCartItem>>

    @PUT("/shoppingCart")
    fun updateProduct(@Body shoppingCartItemToUpdate: ShoppingCartItemToUpdate): Call<ShoppingCartItem>

    @DELETE("/shoppingCart/{id}")
    fun removeProduct(@Path("id") productId: Int): Call<ShoppingCartItem>

    @POST("/shoppingCart")
    fun addProduct(@Body productId: Int): Call<Product>
}