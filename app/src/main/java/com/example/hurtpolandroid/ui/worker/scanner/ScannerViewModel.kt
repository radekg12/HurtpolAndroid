package com.example.hurtpolandroid.ui.worker.scanner

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.hurtpolandroid.ui.model.Product
import com.example.hurtpolandroid.ui.service.WarehouseService
import com.example.hurtpolandroid.ui.utils.HurtpolServiceGenerator
import retrofit2.Call

class ScannerViewModel(var context: Context) : ViewModel() {
    var service = HurtpolServiceGenerator().createServiceWithToken(WarehouseService::class.java, context)

    fun take(id: Long, quantity: Int): Call<Product> {
        return service.take(id, quantity)
    }

    fun put(id: Long, quantity: Int): Call<Product> {
        return service.put(id, quantity)
    }
}