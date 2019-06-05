package com.example.hurtpolandroid.ui.utils

import android.content.Context
import com.example.hurtpolandroid.ui.customer.viewmodels.ProductViewModelFactory
import com.example.hurtpolandroid.ui.model.AppDatabase
import com.example.hurtpolandroid.ui.model.ProductRepository

object InjectionUtils {
    private fun getProductRepository(context: Context): ProductRepository {
        return ProductRepository.getInstance(AppDatabase.getInstance(context.applicationContext).productDao())
    }


    fun provideProductDetailViewModelFactory(
        context: Context,
        productId: Int
    ): ProductViewModelFactory {
        return ProductViewModelFactory(context,
            getProductRepository(context), productId)
    }
}