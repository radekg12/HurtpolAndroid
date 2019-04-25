package com.example.hurtpolandroid.ui.customer.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.widget.NestedScrollView
import com.example.hurtpolandroid.R
import com.example.hurtpolandroid.ui.model.Product
import com.example.hurtpolandroid.ui.service.ProductService
import com.example.hurtpolandroid.ui.utils.HurtpolServiceGenerator
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.logging.Logger

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val logger = Logger.getLogger(HomeActivity::class.java.name)
    val productViews = ArrayList<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        getProducts()
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
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_products -> {
                // Handle the camera action
            }
            R.id.nav_cart -> {

            }
            R.id.nav_logout -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun getProducts() {
        val productService = HurtpolServiceGenerator()
            .createService(ProductService::class.java)

        val context = this
        val scrollView = findViewById<NestedScrollView>(R.id.drawer_layout)
        logger.info("Getting products from API service")
        productService.getProducts()
            .enqueue(object : Callback<List<Product>> {
                override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                    response.body()?.forEach { product ->
                        val productTextView = TextView(context)
                        productTextView.text =
                            java.lang.String.format("Name: %s\n Price: %d", product.name, product.unitPrice)
                        scrollView.addView(productTextView)
                        productTextView.setOnClickListener(fun(it: View) {
                            logger.info(java.lang.String.format("Product with id: %d\" has been clicked", product.id))
                        })
                        productViews.add(productTextView)
                    }
                }

                override fun onFailure(call: Call<List<Product>>, t: Throwable) = t.printStackTrace()
            })
    }
}
