package com.example.hurtpolandroid.ui.model

data class ShoppingCart(
    val content: List<ShoppingCartItem>
)

data class ShoppingCartItem(
    val id: Int,
    val product: Product,
    var quantity: Int
)

data class ShoppingCartItemToUpdate(
    val productId: Int,
    val quantity: Int
)