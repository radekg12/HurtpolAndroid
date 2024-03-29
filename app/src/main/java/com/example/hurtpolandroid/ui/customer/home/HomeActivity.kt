package com.example.hurtpolandroid.ui.customer.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hurtpolandroid.R
import com.example.hurtpolandroid.data.model.Customer
import com.example.hurtpolandroid.data.model.ProductPage
import com.example.hurtpolandroid.ui.customer.shoppingCart.ShoppingCartActivity
import com.example.hurtpolandroid.ui.signin.SigninActivity
import com.example.hurtpolandroid.ui.worker.cardmenu.CardMenuViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.nav_header_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.logging.Logger

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val PRODUCT_ID_MESSAGE = "PRODUCT_ID"
    }

    val logger: Logger = Logger.getLogger(HomeActivity::class.java.name)
    lateinit var homeViewModel: HomeViewModel
    var loading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        initDrawer()
        homeViewModel = HomeViewModel(this)
        nav_view.setNavigationItemSelectedListener(this)

        val linearManager = initRecyclerView()

        val recyclerViewOnScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (hasNextPage())
                    if (hasLastPosition(linearManager)) {
                        recyclerView.setPadding(0, 0, 0, 100)
                        getProducts()
                    }
            }
        }
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener)
        getUserData()
        getProducts()
    }

    private fun hasNextPage() = !homeViewModel.currentPage.last && !loading

    private fun hasLastPosition(linearManager: LinearLayoutManager): Boolean {
        val visibleItemCount = linearManager.childCount
        val totalItemCount = linearManager.itemCount
        val firstVisibleItemPosition = linearManager.findFirstVisibleItemPosition()

        return ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0)
    }

    private fun initDrawer() {
        val drawerToggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
    }


    private fun initRecyclerView(): LinearLayoutManager {
        val linearManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = ProductAdapter(this, homeViewModel.productList)
        recyclerView.layoutManager = linearManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        return linearManager
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            moveTaskToBack(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_products -> {
            }
            R.id.nav_cart -> {
                val intent = Intent(this, ShoppingCartActivity::class.java)
                startActivity(intent)
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
        loading = true
        loadingProductsProgressBar.visibility = View.VISIBLE
        var nextPage = 0
        if (homeViewModel.productList.isNotEmpty()) {
            nextPage = homeViewModel.currentPage.number + 1
        }
        logger.info("Getting products from API service with pageNumber=$nextPage")

        homeViewModel.getProducts(nextPage).enqueue(object : Callback<ProductPage> {
            override fun onFailure(call: Call<ProductPage>, t: Throwable) {
                onProductPageFailure(t)
            }

            override fun onResponse(call: Call<ProductPage>, response: Response<ProductPage>) {
                onProductPageResponse(response)
            }
        })
    }

    private fun onProductPageFailure(t: Throwable) {
        Toast.makeText(this@HomeActivity, getString(R.string.server_error), Toast.LENGTH_LONG).show()
        loading = false
        loadingProductsProgressBar.visibility = View.INVISIBLE
        logger.warning(t.printStackTrace().toString())
    }

    private fun onProductPageResponse(response: Response<ProductPage>) {
        homeViewModel.currentPage = response.body()!!
        homeViewModel.currentPage.content.forEach { product -> homeViewModel.productList.add(product) }
        recyclerView.adapter?.notifyDataSetChanged()
        loading = false
        recyclerView.setPadding(0, 0, 0, 0)
        loadingProductsProgressBar.visibility = View.INVISIBLE
    }

    private fun getUserData() {
        val cardMenuViewModel = CardMenuViewModel(this)
        cardMenuViewModel.getUser().enqueue(object : Callback<Customer> {
            override fun onResponse(call: Call<Customer>, response: Response<Customer>) {
                if (response.isSuccessful) {
                    val fullName = response.body()?.firstName + " " + response.body()?.lastName
                    user_name.text = fullName
                    user_email.text = response.body()?.email
                }
            }

            override fun onFailure(call: Call<Customer>, t: Throwable) {
                logger.warning(t.printStackTrace().toString())
            }
        })
    }
}
