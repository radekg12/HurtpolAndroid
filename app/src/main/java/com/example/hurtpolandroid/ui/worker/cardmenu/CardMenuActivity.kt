package com.example.hurtpolandroid.ui.worker.cardmenu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.hurtpolandroid.R
import com.example.hurtpolandroid.ui.model.CustomerDTO
import com.example.hurtpolandroid.ui.signin.SigninActivity
import com.example.hurtpolandroid.ui.worker.OperationType
import com.example.hurtpolandroid.ui.worker.scanner.ScannerActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_card_menu.*
import kotlinx.android.synthetic.main.app_bar_card_menu.*
import kotlinx.android.synthetic.main.content_card_menu.*
import kotlinx.android.synthetic.main.nav_header_card_menu.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CardMenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, Callback<CustomerDTO> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_menu)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view_worker.setNavigationItemSelectedListener(this)

        card1.setOnClickListener {
            changeActivity(OperationType.TAKE)
        }
        card2.setOnClickListener {
            changeActivity(OperationType.PUT)
        }
        getUserData()
    }

    private fun changeActivity(operation: OperationType) {
        val intent = Intent(this, ScannerActivity::class.java).apply {
            putExtra("Operation", operation)
        }
        startActivity(intent)
    }

    private fun getUserData() {
        val cardMenuViewModel = CardMenuViewModel(this)
        cardMenuViewModel.getUser().enqueue(this)
    }

    override fun onFailure(call: Call<CustomerDTO>, t: Throwable) {
        println("blad")
    }

    override fun onResponse(call: Call<CustomerDTO>, response: Response<CustomerDTO>) {
        if (response.isSuccessful) {
            val fullName = response.body()?.firstName + " " + response.body()?.lastName
            worker_name.text = fullName
            worker_email.text = response.body()?.email
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            moveTaskToBack(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.card_menu, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_products -> {
                changeActivity(OperationType.TAKE)
            }
            R.id.nav_cart -> {
                changeActivity(OperationType.PUT)
            }
            R.id.logout -> {
                val intent = Intent(this, SigninActivity::class.java).apply {
                    val preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                    preferences.edit().remove("token").apply()
                }
                startActivity(intent)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
