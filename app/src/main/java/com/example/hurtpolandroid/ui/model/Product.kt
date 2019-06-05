package com.example.hurtpolandroid.ui.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "product")
data class Product(
    @PrimaryKey var id: Int,
    var name: String,
    var description: String,
    var company: String,
    var quantityInStock: Int,
    var unitPrice: Int,
    var imageUrl: String,

    @Relation(parentColumn = "id", entityColumn =  "product_id")
    val specificationPositions: List<Specification>
) {
    constructor() : this(0, "", "", "", 0, 0, "", emptyList())
}

