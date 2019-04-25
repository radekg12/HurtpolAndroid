package com.example.hurtpolandroid.ui.signin

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hurtpolandroid.R
import com.example.hurtpolandroid.ui.customer.home.HomeActivity
import com.example.hurtpolandroid.ui.signin.domain.model.Authority
import com.example.hurtpolandroid.ui.signin.domain.model.SigninResponse
import com.example.hurtpolandroid.ui.signup.SignupActivity
import com.example.hurtpolandroid.ui.worker.cardmenu.CardMenuActivity
import kotlinx.android.synthetic.main.activity_signin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninActivity : AppCompatActivity(), Callback<SigninResponse> {

    val loginViewModel = SigninViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        btn_login.setOnClickListener {
            login()
        }

        link_signup.setOnClickListener {
            val intent = Intent(getApplicationContext(), SignupActivity::class.java);
            startActivityForResult(intent, 0)
        }


        object : CountDownTimer(3000, 1000) {
            override fun onFinish() {
                loadingProgressBar.visibility = View.GONE
                if (!loginViewModel.isLogged())
                    slideUp(bookIconImageView)
                else {
                    //TODO autologowanie, poprawic jak nowe logowanie bedzie na prod
                    val list = mutableListOf<Authority>()
//                    list.add(Authority("ROLE_WORKER"))
                    list.add(Authority("ROLE_USER"))
                    val data = SigninResponse("ABC", "abc", true, list)
                    onLoginSuccess(data)
                }
            }

            override fun onTick(p0: Long) {}
        }.start()
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

    fun login() {

        if (!validate())
            onLoginFailed();

        btn_login.isEnabled = false
        loadingProgressBar.visibility = View.VISIBLE
        val email = input_email.text.toString()
        val password = input_password.text.toString()
        loginViewModel.login(email, password).enqueue(this)
    }

    override fun onFailure(call: Call<SigninResponse>, t: Throwable) {
        onServerProblems();
    }

    override fun onResponse(call: Call<SigninResponse>, response: Response<SigninResponse>) {
        if (response.isSuccessful) {
            val data = response.body() as SigninResponse
            onLoginSuccess(data)
        } else {
            onLoginFailed()
        }
    }

    fun onLoginSuccess(data: SigninResponse) {
        btn_login.isEnabled = true
        if (data.authorities.map { it.roleName }.any { it.equals("ROLE_WORKER") }) {
            val intent = Intent(this, CardMenuActivity::class.java).apply {
                putExtra("Token", data.accessToken)
            }
            startActivity(intent)
        } else if (data.authorities.map { it.roleName }.any { it.equals("ROLE_USER") }) {
            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra("Token", data.accessToken)
            }
            startActivity(intent)
        }
    }

    fun onLoginFailed() {
        Toast.makeText(getBaseContext(), "Logowanie nieudane", Toast.LENGTH_LONG).show()
        loadingProgressBar.visibility = View.GONE
        btn_login.isEnabled = true
    }

    private fun onServerProblems() {
        Toast.makeText(getBaseContext(), "Błąd serwera", Toast.LENGTH_LONG).show()
        loadingProgressBar.visibility = View.GONE
        btn_login.isEnabled = true
    }

    fun validate(): Boolean {
        var valid = true

        val email = input_email.text.toString()
        val password = input_password.text.toString()

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.error = "Błędy adres e-maill"
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

        return valid
    }
}