package com.example.hurtpolandroid.ui.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hurtpolandroid.R
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        btn_signup.setOnClickListener {
            signup()
        }

        link_login.setOnClickListener {
            // Finish the registration screen and return to the Login activity
//            finish();
        }
    }

    fun signup() {

        if (!validate()) {
            onSignupFailed()
            return
        }

        btn_signup.isEnabled = false
        loadingProgressBar.visibility = View.VISIBLE

        val name = input_name.getText().toString()
        val email = input_email.getText().toString()
        val password = input_password.getText().toString()

    }


    fun onSignupSuccess() {
        btn_signup.setEnabled(true)
        setResult(RESULT_OK, null)
        btn_signup.isEnabled = true
        loadingProgressBar.visibility = View.GONE
//        finish();
    }

    fun onSignupFailed() {
        Toast.makeText(getBaseContext(), "Logowanie nieudane", Toast.LENGTH_LONG).show()
        btn_signup.isEnabled = true
        loadingProgressBar.visibility = View.GONE
    }

    fun validate(): Boolean {
        var valid = true;

        val name = input_name.getText().toString()
        val email = input_email.getText().toString()
        val password = input_password.getText().toString()

        if (name.isEmpty() || name.length < 3) {
            input_name.setError("Minimum 3 znaki")
            valid = false
        } else {
            input_name.setError(null)
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.setError("Błędy adres e-mail")
            valid = false
        } else {
            input_email.setError(null)
        }

        if (password.isEmpty() || password.length < 4 || password.length > 10) {
            input_password.setError("Od 4 do 10 znaków")
            valid = false
        } else {
            input_password.setError(null)
        }

        return valid;
    }
}