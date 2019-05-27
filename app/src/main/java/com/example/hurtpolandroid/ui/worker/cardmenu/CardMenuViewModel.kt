package com.example.hurtpolandroid.ui.worker.cardmenu

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.hurtpolandroid.ui.model.CustomerDTO
import com.example.hurtpolandroid.ui.service.CustomerService
import com.example.hurtpolandroid.ui.utils.HurtpolServiceGenerator
import retrofit2.Call

class CardMenuViewModel(var context: Context) : ViewModel() {
    private var service = HurtpolServiceGenerator().createServiceWithToken(CustomerService::class.java, context)

    fun getUser(): Call<CustomerDTO> {
        return service.getUser()
    }
}