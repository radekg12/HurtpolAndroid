package com.example.hurtpolandroid.ui.worker.cardmenu

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.hurtpolandroid.data.model.Customer
import com.example.hurtpolandroid.service.CustomerService
import com.example.hurtpolandroid.utils.HurtpolServiceGenerator
import retrofit2.Call

class CardMenuViewModel(var context: Context) : ViewModel() {
    private var customerService = HurtpolServiceGenerator().createServiceWithToken(CustomerService::class.java, context)

    fun getUser(): Call<Customer> {
        return customerService.getUser()
    }
}