package com.example.hurtpolandroid.ui.customer.productDetail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hurtpolandroid.R
import com.example.hurtpolandroid.ui.customer.home.HomeActivity
import com.example.hurtpolandroid.ui.customer.viewmodels.ProductDetailViewModel
import com.example.hurtpolandroid.ui.model.Product
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.content_product_detail.*
import java.util.logging.Logger


class ProductDetailActivity : AppCompatActivity() {

    val logger: Logger = Logger.getLogger(ProductDetailActivity::class.java.name)
    var productDetail: Product? = null
    lateinit var model: ProductDetailViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        setSupportActionBar(toolbar)

        model = ProductDetailViewModel(this)
        val linearManager = LinearLayoutManager(this)
        specification.setHasFixedSize(true)
        specification.layoutManager = linearManager
        specification.itemAnimator = DefaultItemAnimator()
        specification.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        ViewCompat.setNestedScrollingEnabled(specification, false)

        val productID = intent?.extras!!.getInt(HomeActivity.PRODUCT_ID_MESSAGE)
        if (productID == null) {
            Toast.makeText(this, getString(R.string.null_product_id), Toast.LENGTH_LONG).show()
            finish()
        } else {
            getProductDetail()
        }
        add_product_button.setOnClickListener {
            model.addProductToShoppingCart(productID)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        model.shoppingCartResponse.observe(this,
            Observer<Product> {
                Toast.makeText(
                    this@ProductDetailActivity,
                    getString(R.string.addedToCart),
                    Toast.LENGTH_LONG
                ).show()
            })

    }

    private fun getProductDetail() {
        model.getProductDetail()
    }


}
