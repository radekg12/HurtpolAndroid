package com.example.hurtpolandroid.service

import com.example.hurtpolandroid.data.model.Product
import com.example.hurtpolandroid.data.model.ShoppingCartItem
import com.example.hurtpolandroid.data.model.ShoppingCartItemToUpdate
import retrofit2.Call
import retrofit2.http.*

interface ShoppingCartService {
    @GET("shopping-carts")
    fun getShoppingCart(): Call<ArrayList<ShoppingCartItem>>

    @PUT("shopping-carts")
    fun updateShoppingCardItem(@Body shoppingCartItemToUpdate: ShoppingCartItemToUpdate): Call<ShoppingCartItem>

    @DELETE("shopping-carts/{id}")
    fun removeShoppingCardItem(@Path("id") productId: Int): Call<ShoppingCartItem>

    @POST("shopping-carts")
    fun addShoppingCardItem(@Body productId: Int): Call<Product>
}