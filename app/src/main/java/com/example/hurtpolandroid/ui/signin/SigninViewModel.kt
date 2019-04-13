package com.example.hurtpolandroid.ui.signin

import androidx.lifecycle.ViewModel
import com.example.hurtpolandroid.ui.signin.domain.model.SigninResponse
import com.example.hurtpolandroid.ui.signin.domain.model.UserDTO
import com.example.hurtpolandroid.ui.signin.domain.service.AuthenticationService
import com.example.hurtpolandroid.ui.utils.HurtpolServiceGenerator
import retrofit2.Call


class SigninViewModel : ViewModel() {

    var service = HurtpolServiceGenerator().createService(AuthenticationService::class.java)

    fun login(username: String, password: String): Call<SigninResponse> {
        val user = UserDTO(username, password)
        return service.signin(user)
    }

    fun isLogged(): Boolean {
        //TODO sprawdzenie czy zalogowany
        return true
    }
}