package com.example.hurtpolandroid.ui.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(
    @PrimaryKey var id: Int,
    var name: String,
    var description: String,
    var company: String,
    var quantityInStock: Int,
    var unitPrice: Int,
    var imageUrl: String,
    @Ignore var specificationPositions: List<Specification> = emptyList()
) {
    constructor() : this(0, "", "", "", 0,
        0, "", emptyList())
}

