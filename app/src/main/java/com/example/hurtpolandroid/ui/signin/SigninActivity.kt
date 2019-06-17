package com.example.hurtpolandroid.ui.signin

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Patterns
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.auth0.android.jwt.JWT
import com.example.hurtpolandroid.R
import com.example.hurtpolandroid.data.model.Authority
import com.example.hurtpolandroid.data.model.Role
import com.example.hurtpolandroid.data.model.SigninResponse
import com.example.hurtpolandroid.ui.customer.home.HomeActivity
import com.example.hurtpolandroid.ui.signup.SignupActivity
import com.example.hurtpolandroid.ui.worker.cardmenu.CardMenuActivity
import kotlinx.android.synthetic.main.activity_signin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SigninActivity : AppCompatActivity(), Callback<SigninResponse> {

    val signinViewModel = SigninViewModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        btn_login.setOnClickListener {
            login()
        }

        link_signup.setOnClickListener {
            navigateToSignup()
        }

        object : CountDownTimer(3000, 1000) {
            override fun onFinish() {
                loadingProgressBar.visibility = View.GONE
                val token = signinViewModel.getToken()
                if (!signinViewModel.isLogged(token))
                    slideUp(bookIconImageView)
                else {
                    val jwt = JWT(token)
                    val authorities = mutableListOf<Authority>()
                    authorities.add(Authority(jwt.claims["auth"]?.asString().toString()))
                    val data = SigninResponse(jwt.signature, "Bearer", true, authorities)
                    onLoginSuccess(data)
                }
            }

            override fun onTick(p0: Long) {}
        }.start()
    }

    private fun navigateToSignup() {
        val intent = Intent(applicationContext, SignupActivity::class.java)
        startActivityForResult(intent, 0)
    }

    fun slideUp(view: View) {
        val animate = AnimationUtils.loadAnimation(this, R.anim.move_up)
        animate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                afterAnimationView.visibility = View.VISIBLE
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })
        view.startAnimation(animate)
    }

    private fun login() {

        if (!validateForm())
            onLoginFailed()

        showProgressBar()
        val email = input_email.text.toString()
        val password = input_password.text.toString()
        signinViewModel.login(email, password).enqueue(this)
    }

    private fun showProgressBar() {
        btn_login.isEnabled = false
        loadingProgressBar.visibility = View.VISIBLE
    }

    override fun onFailure(call: Call<SigninResponse>, t: Throwable) {
        onServerProblems()
    }

    override fun onResponse(call: Call<SigninResponse>, response: Response<SigninResponse>) {
        if (response.isSuccessful) {
            val data = response.body() as SigninResponse
            signinViewModel.saveToken(response.body()?.accessToken)
            onLoginSuccess(data)
        } else {
            onLoginFailed()
        }
    }

    fun onLoginSuccess(data: SigninResponse) {
        btn_login.isEnabled = true
        if (hasWorkerRole(data)) {
            navigateToWorkerPanel(data)
        } else if (hasUserRole(data)) {
            navigateToCustomerPanel(data)
        }
    }

    private fun navigateToCustomerPanel(data: SigninResponse) {
        val intent = Intent(this, HomeActivity::class.java).apply {
            putExtra("Token", data.accessToken)
        }
        startActivity(intent)
    }

    private fun hasUserRole(data: SigninResponse) =
        data.authorities.map { it.roleName }.any { it == Role.ROLE_USER.name }

    private fun hasWorkerRole(data: SigninResponse) =
        data.authorities.map { it.roleName }.any { it == Role.ROLE_WORKER.name }

    private fun navigateToWorkerPanel(data: SigninResponse) {
        val intent = Intent(this, CardMenuActivity::class.java).apply {
            putExtra("Token", data.accessToken)
        }
        startActivity(intent)
    }

    private fun onLoginFailed() {
        Toast.makeText(baseContext, getString(R.string.login_failed), Toast.LENGTH_LONG).show()
        loadingProgressBar.visibility = View.GONE
        btn_login.isEnabled = true
    }

    private fun onServerProblems() {
        Toast.makeText(baseContext, getString(R.string.invalid_value), Toast.LENGTH_LONG).show()
        loadingProgressBar.visibility = View.GONE
        btn_login.isEnabled = true
    }

    private fun validateForm(): Boolean {
        val email = input_email.text.toString()
        val password = input_password.text.toString()
        return validateEmail(email) && validatePassword(password)
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
}