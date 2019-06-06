package com.example.hurtpolandroid.ui.customer.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hurtpolandroid.ui.model.ProductRepository

class ProductViewModelFactory(
    private val context: Context,
    private val repository: ProductRepository,
    private val productId: Int
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CASE")
    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        return ProductDetailViewModel(context, repository, productId) as T
    }
}