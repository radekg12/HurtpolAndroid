package com.example.hurtpolandroid.ui.signup

import androidx.lifecycle.ViewModel
import com.example.hurtpolandroid.ui.service.AuthenticationService
import com.example.hurtpolandroid.ui.model.Account
import com.example.hurtpolandroid.ui.utils.HurtpolServiceGenerator
import retrofit2.Call

class SignupViewModel : ViewModel() {
    var service = HurtpolServiceGenerator().createService(AuthenticationService::class.java)

    fun registration(firstName: String, lastName: String, email: String, password: String): Call<Boolean> {
        val account = Account(firstName, lastName, email, password)
        return service.signup(account)
    }
}