package com.example.hurtpolandroid.ui.signin

import android.content.Context
import androidx.lifecycle.ViewModel
import com.auth0.android.jwt.JWT
import com.example.hurtpolandroid.data.model.SigninResponse
import com.example.hurtpolandroid.data.model.UserDTO
import com.example.hurtpolandroid.service.AuthenticationService
import com.example.hurtpolandroid.utils.HurtpolServiceGenerator
import retrofit2.Call
import java.util.*


class SigninViewModel(var context: Context) : ViewModel() {

    var service = HurtpolServiceGenerator().createService(AuthenticationService::class.java)

    fun login(username: String, password: String): Call<SigninResponse> {
        val user = UserDTO(username, password)
        return service.signin(user)
    }

    fun isLogged(token: String): Boolean {
        return if (token != "") {
            val jwt = JWT(token)
            val isActive = jwt.expiresAt!!.after(Calendar.getInstance().time)
            token != "" && isActive
        } else false
    }

    fun getToken(): String {
        val preferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        return preferences.getString("token", "").orEmpty()
    }

    fun saveToken(accessToken: String?) {
        val preferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        preferences.edit().putString("token", accessToken).apply()
    }
}