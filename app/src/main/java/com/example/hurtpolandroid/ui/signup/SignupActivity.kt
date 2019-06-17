package com.example.hurtpolandroid.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hurtpolandroid.R
import com.example.hurtpolandroid.data.model.Customer
import com.example.hurtpolandroid.ui.signin.SigninActivity
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity(), Callback<Customer> {

    private val signupViewModel = SignupViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        loadingProgressBar.visibility = View.GONE

        btn_signup.setOnClickListener {
            signup()
        }

        link_login.setOnClickListener {
            navigateToSignin()
        }

    }

    private fun navigateToSignin() {
        val intent = Intent(applicationContext, SigninActivity::class.java)
        startActivityForResult(intent, 0)
    }

    private fun signup() {

        if (!validateForm())
            onSignupFailed()
        showProgressBar()

        val firstname = input_firstname.text.toString()
        val lastname = input_lastname.text.toString()
        val email = input_email.text.toString()
        val password = input_password.text.toString()

        signupViewModel.registration(firstname, lastname, email, password).enqueue(this)
    }

    private fun showProgressBar() {
        btn_signup.isEnabled = false
        loadingProgressBar.visibility = View.VISIBLE
    }

    override fun onFailure(call: Call<Customer>, t: Throwable) {
        onSignupFailed()
    }

    override fun onResponse(call: Call<Customer>, response: Response<Customer>) {
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
        Toast.makeText(baseContext, getString(R.string.registration_completed), Toast.LENGTH_LONG).show()
        val intent = Intent(applicationContext, SigninActivity::class.java)
        startActivityForResult(intent, 0)
    }

    private fun onSignupFailed() {
        Toast.makeText(baseContext, getString(R.string.registration_failed), Toast.LENGTH_LONG).show()
        btn_signup.isEnabled = true
        loadingProgressBar.visibility = View.GONE
    }

    private fun validateForm(): Boolean {
        val firstname = input_firstname.text.toString()
        val lastname = input_lastname.text.toString()
        val email = input_email.text.toString()
        val password = input_password.text.toString()

        return validateFirstName(firstname)
                && validateLastName(lastname)
                && validateEmail(email)
                && validatePassword(password)
    }

    private fun validatePassword(password: String): Boolean {
        return if (password.isEmpty() || password.length < 4 || password.length > 10) {
            input_password.error = getString(R.string.input_password_error)
            false
        } else {
            input_password.error = null
            true
        }
    }

    private fun validateEmail(email: String): Boolean {
        return if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.error = getString(R.string.incorrect_email_address)
            false
        } else {
            input_email.error = null
            true
        }
    }

    private fun validateLastName(lastname: String): Boolean {
        return if (lastname.isEmpty() || lastname.length < 3) {
            input_lastname.error = getString(R.string.input_last_name_error)
            false
        } else {
            input_lastname.error = null
            true
        }
    }

    private fun validateFirstName(firstname: String): Boolean {
        if (firstname.isEmpty() || firstname.length < 3) {
            input_firstname.error = getString(R.string.input_first_name_error)
            return false
        } else {
            input_firstname.error = null
        }
        return true
    }
}