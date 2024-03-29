package com.example.hurtpolandroid.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.example.hurtpolandroid.data.model.Product
import com.example.hurtpolandroid.data.model.Specification

@Dao
interface ProductDao {

    @Transaction
    fun insertProductWithSpecification(product: Product) {
        val specifications = product.specificationPositions
        for(specification in specifications) {
            specification.productId = product.id
        }

        insetSpecification(specifications)
        insert(product)
    }

    @Insert(onConflict = REPLACE)
    fun save(product: Product)

    @Query("SELECT * FROM product WHERE id = :productId")
    fun getByID(productId: Int): LiveData<Product>

    @Query("SELECT COUNT(*) FROM product WHERE id = :productId")
    fun hasProduct(productId: Int): Int

    @Query("SELECT * FROM specification WHERE productId = :productId")
    fun getBySpecificationId(productId: Int): LiveData<List<Specification>>

    @Insert(onConflict = REPLACE)
    fun insert(product: Product)

    @Insert(onConflict = REPLACE)
    fun insetSpecification(specification: List<Specification>)

}