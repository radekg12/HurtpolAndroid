package com.example.hurtpolandroid.data.model

data class ProductPage(
    val content: List<Product>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val size: Int,
    val totalElements: Int,
    val totalPages: Int
)