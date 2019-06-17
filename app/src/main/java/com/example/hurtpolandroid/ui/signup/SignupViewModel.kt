package com.example.hurtpolandroid.ui.signup

import androidx.lifecycle.ViewModel
import com.example.hurtpolandroid.data.model.Account
import com.example.hurtpolandroid.data.model.Customer
import com.example.hurtpolandroid.service.AuthenticationService
import com.example.hurtpolandroid.utils.HurtpolServiceGenerator
import retrofit2.Call

class SignupViewModel : ViewModel() {
    var service = HurtpolServiceGenerator().createService(AuthenticationService::class.java)

    fun registration(firstName: String, lastName: String, email: String, password: String): Call<Customer> {
        val account = Account(firstName, lastName, email, password)
        return service.signup(account)
    }
}