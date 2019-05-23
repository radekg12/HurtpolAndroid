package com.example.hurtpolandroid.ui.model

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

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val company: String,
    val quantityInStock: Int,
    val unitPrice: Int,
    val imageUrl: String,
    val specificationPositions: List<Specification>
)

data class Specification(val id: Int, val name: String, val value: String)