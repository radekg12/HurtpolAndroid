package com.example.hurtpolandroid.ui.customer.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.hurtpolandroid.R
import com.example.hurtpolandroid.ui.customer.productDetail.ProductDetailActivity
import com.example.hurtpolandroid.ui.model.Content
import com.example.hurtpolandroid.ui.model.Product
import com.example.hurtpolandroid.ui.signin.SigninActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.logging.Logger

class HomeActivity : AppCompatActivity(), Callback<Content<Product>>, NavigationView.OnNavigationItemSelectedListener {
    companion object {
        const val PROUCT_ID_MESSAGE = "PRODUCT_ID"
    }

    val logger = Logger.getLogger(HomeActivity::class.java.name)
    val productViews = ArrayList<TextView>()
    var currentPageNumber = 0
    var pageNumberMax = 100

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

        val pageNumberText = findViewById<TextView>(R.id.page_number_text)
        pageNumberText.text = currentPageNumber.toString()
        val prevButton = findViewById<Button>(R.id.prev_page_button)
        prevButton.setOnClickListener {
            currentPageNumber = Math.max(currentPageNumber - 1, 0)
            pageNumberText.text = currentPageNumber.toString()
            getProducts()
        }
        val nextButton = findViewById<Button>(R.id.next_page_button)
        nextButton.setOnClickListener {
            currentPageNumber = Math.min(currentPageNumber + 1, pageNumberMax)
            pageNumberText.text = currentPageNumber.toString()
            getProducts()
        }

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

    private fun getProducts() {
        logger.info("Getting products from API service with pageNumber=$currentPageNumber")
        cleanProductList()
        val homeViewModel = HomeViewModel()
        homeViewModel.getProducts(currentPageNumber).enqueue(this)
    }

    override fun onFailure(call: Call<Content<Product>>, t: Throwable) {
        Toast.makeText(this, getString(R.string.server_error), Toast.LENGTH_LONG).show()
        logger.warning(t.printStackTrace().toString())
    }

    override fun onResponse(call: Call<Content<Product>>, response: Response<Content<Product>>) {
        response.body()?.content?.forEach { product ->
            logger.info("Processing product with id " + product.id)
            val productTextView = TextView(this)
            productTextView.text =
                java.lang.String.format("Name: ${product.name} Price: ${product.unitPrice}")

            productTextView.setOnClickListener {
                logger.info(java.lang.String.format("Product with id: ${product.id} has been clicked"))
                productOnClick(product.id)
            }
            productTextView.gravity = Gravity.CENTER
            products_list.addView(productTextView)
            productViews.add(productTextView)
        }
        if (productViews.size == 0) {
            val nonProductText = TextView(this)
            nonProductText.text = getString(R.string.empty_product_list)
            pageNumberMax = currentPageNumber
            products_list.addView(nonProductText)
        }
    }

    fun productOnClick(productID: Int) {
        val intent = Intent(this, ProductDetailActivity::class.java).apply {
            putExtra(PROUCT_ID_MESSAGE, productID)
        }
        startActivity(intent)
    }

    private fun cleanProductList() {
        val homeView = findViewById<LinearLayout>(R.id.products_list)
        homeView.removeAllViewsInLayout()
        productViews.clear()
    }
}
