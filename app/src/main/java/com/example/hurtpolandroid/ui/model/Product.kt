package com.example.hurtpolandroid.ui.model

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val company: String,
    val quantityInStock: Int,
    val unitPrice: Int,
    val imageUrl: String
)