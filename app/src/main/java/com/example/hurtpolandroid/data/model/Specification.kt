package com.example.hurtpolandroid.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "specification")
data class Specification(
    @PrimaryKey var id: Int,
    var name: String,
    var value: String,
    var productId: Int
)