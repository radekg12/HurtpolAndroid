package com.example.hurtpolandroid.ui.customer.productDetail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.hurtpolandroid.R
import com.example.hurtpolandroid.ui.customer.home.HomeActivity
import com.example.hurtpolandroid.ui.model.Product
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.content_product_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.logging.Logger


class ProductDetailActivity : AppCompatActivity() {

    val logger = Logger.getLogger(ProductDetailActivity::class.java.name)
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

        val productID = intent?.extras!!.getInt(HomeActivity.PROUCT_ID_MESSAGE)
        if (productID == null) {
            Toast.makeText(this, getString(R.string.null_product_id), Toast.LENGTH_LONG).show()
            finish()
        } else {
            getProductDetail(productID)
        }
        add_product_button.setOnClickListener {
            model.addProductToShoppingCart(productID)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        model.shoppingCartResponse.observe(this, object: Observer<Product>{
            override fun onChanged(t: Product?) {
                Toast.makeText(this@ProductDetailActivity, getString(R.string.addedToCart), Toast.LENGTH_LONG).show()
            }

        })

    }

    private fun getProductDetail(productID: Int) {
        model.getProductDetail(productID).enqueue(object : Callback<Product> {
            override fun onFailure(call: Call<Product>, t: Throwable) {
                Toast.makeText(this@ProductDetailActivity, getString(R.string.server_error), Toast.LENGTH_LONG).show()
                loadingProduct.visibility = View.GONE
                logger.warning(t.printStackTrace().toString())
            }

            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                productDetail = response.body()!!
                if (productDetail != null) {
                    toolbar.title = productDetail?.name
                    product_name.text = productDetail?.name
                    product_desc.text = productDetail?.description
                    val format = NumberFormat.getCurrencyInstance()
                    val currency = format.format(productDetail?.unitPrice?.div(100))
                    product_price.text = currency
                    Glide.with(this@ProductDetailActivity)
                        .load(productDetail?.imageUrl)
                        .into(product_image)

                    if (productDetail!!.specificationPositions.isNotEmpty()) {
                        specification_sec.visibility = View.VISIBLE
                        specification.adapter =
                            SpecificationAdapter(this@ProductDetailActivity, productDetail?.specificationPositions!!)
                    }
                } else {
                    product_desc.text = getString(R.string.null_product_id)
                }
                loadingProduct.visibility = View.GONE
            }
        })
    }


}
