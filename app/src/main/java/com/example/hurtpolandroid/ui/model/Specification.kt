package com.example.hurtpolandroid.ui.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "specification",
    foreignKeys = [ForeignKey(entity = Product::class, parentColumns = ["id"], childColumns = ["product_id"])]
)
data class Specification(
    val id: Int,
    val name: String,
    val value: String,
    @ColumnInfo(name = "product_id") val productId: Int
)