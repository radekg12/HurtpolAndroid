package com.example.hurtpolandroid.ui.worker.scanner

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.hurtpolandroid.data.model.Product
import com.example.hurtpolandroid.data.model.WarehouseStateUpdate
import com.example.hurtpolandroid.service.WarehouseService
import com.example.hurtpolandroid.utils.HurtpolServiceGenerator
import retrofit2.Call

class ScannerViewModel(var context: Context) : ViewModel() {
    private var warehouseService =
        HurtpolServiceGenerator().createServiceWithToken(WarehouseService::class.java, context)

    fun take(id: Int, quantity: Int): Call<Product> {
        val warehouseStateUpdate = WarehouseStateUpdate(id, -1 * quantity)
        return warehouseService.take(warehouseStateUpdate)
    }

    fun put(id: Int, quantity: Int): Call<Product> {
        val warehouseStateUpdate = WarehouseStateUpdate(id, quantity)
        return warehouseService.put(warehouseStateUpdate)
    }
}