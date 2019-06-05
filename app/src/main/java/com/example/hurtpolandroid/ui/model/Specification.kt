package com.example.hurtpolandroid.ui.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "specification")
data class Specification(
    @PrimaryKey val id: Int,
    val name: String,
    val value: String,
    val productId: Int
)