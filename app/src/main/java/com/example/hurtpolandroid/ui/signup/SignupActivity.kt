package com.example.hurtpolandroid.ui.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hurtpolandroid.R
import com.example.hurtpolandroid.ui.model.CustomerDTO
import com.example.hurtpolandroid.ui.signin.SigninActivity
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity(), Callback<CustomerDTO> {

    private val signupViewModel = SignupViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        loadingProgressBar.visibility = View.GONE

        btn_signup.setOnClickListener {
            signup()
        }

        link_login.setOnClickListener {
            val intent = Intent(applicationContext, SigninActivity::class.java)
            startActivityForResult(intent, 0)
        }

    }

    private fun signup() {

        if (!validate())
            onSignupFailed()

        btn_signup.isEnabled = false
        loadingProgressBar.visibility = View.VISIBLE

        val firstname = input_firstname.text.toString()
        val lastname = input_lastname.text.toString()
        val email = input_email.text.toString()
        val password = input_password.text.toString()

        signupViewModel.registration(firstname, lastname, email, password).enqueue(this)
    }

    override fun onFailure(call: Call<CustomerDTO>, t: Throwable) {
        onSignupFailed()
    }

    override fun onResponse(call: Call<CustomerDTO>, response: Response<CustomerDTO>) {
        if (response.isSuccessful) {
            onSignupSuccess()
        } else {
            onSignupFailed()
        }
    }


    private fun onSignupSuccess() {
        btn_signup.isEnabled = true
        setResult(RESULT_OK, null)
        btn_signup.isEnabled = true
        loadingProgressBar.visibility = View.GONE
        Toast.makeText(baseContext, "Rejestracja zakończona powodzeniem", Toast.LENGTH_LONG).show()
        val intent = Intent(applicationContext, SigninActivity::class.java)
        startActivityForResult(intent, 0)
    }

    private fun onSignupFailed() {
        Toast.makeText(baseContext, "Rejestracja nieudana", Toast.LENGTH_LONG).show()
        btn_signup.isEnabled = true
        loadingProgressBar.visibility = View.GONE
    }

    private fun validate(): Boolean {
        var valid = true

        val firstname = input_firstname.text.toString()
        val lastname = input_lastname.text.toString()
        val email = input_email.text.toString()
        val password = input_password.text.toString()

        if (firstname.isEmpty() || firstname.length < 3) {
            input_firstname.error = "Minimum 3 znaki"
            valid = false
        } else {
            input_firstname.error = null
        }

        if (lastname.isEmpty() || lastname.length < 3) {
            input_lastname.error = "Minimum 3 znaki"
            valid = false
        } else {
            input_lastname.error = null
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.error = "Błędy adres e-mail"
            valid = false
        } else {
            input_email.error = null
        }

        if (password.isEmpty() || password.length < 4 || password.length > 10) {
            input_password.error = "Od 4 do 10 znaków"
            valid = false
        } else {
            input_password.error = null
        }

        return valid
    }
}