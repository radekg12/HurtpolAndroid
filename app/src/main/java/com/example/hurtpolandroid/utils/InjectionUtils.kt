package com.example.hurtpolandroid.utils

import android.content.Context
import com.example.hurtpolandroid.data.AppDatabase
import com.example.hurtpolandroid.data.ProductRepository
import com.example.hurtpolandroid.ui.customer.viewmodels.ProductViewModelFactory

object InjectionUtils {
    private fun getProductRepository(context: Context): ProductRepository {
        return ProductRepository.getInstance(AppDatabase.getInstance(context.applicationContext).productDao())
    }


    fun provideProductDetailViewModelFactory(
        context: Context,
        productId: Int
    ): ProductViewModelFactory {
        return ProductViewModelFactory(
            context,
            getProductRepository(context), productId
        )
    }
}