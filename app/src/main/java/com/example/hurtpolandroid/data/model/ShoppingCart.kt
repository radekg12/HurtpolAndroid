package com.example.hurtpolandroid.data.model

data class ShoppingCart(
    val content: List<ShoppingCartItem>
)

data class ShoppingCartItem(
    val id: Int,
    var quantity: Int,
    val product: Product
)

data class ShoppingCartItemToUpdate(
    val productId: Int,
    val quantity: Int,
    var product: Product = Product()
)