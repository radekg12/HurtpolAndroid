package com.example.hurtpolandroid.ui.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.hurtpolandroid.ui.model.Product

@Dao
interface ProductDao{

    @Insert(onConflict = REPLACE)
    fun save(product: Product)

    @Query("SELECT * FROM product WHERE id = :productId")
    fun load(productId: Int): LiveData<Product>

    @Query("SELECT COUNT(*) FROM product WHERE id = :productId")
    fun hasProduct(productId: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: Product)
}