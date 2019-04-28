package com.example.hurtpolandroid.ui.customer.home

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hurtpolandroid.R
import com.example.hurtpolandroid.ui.model.Product
import com.example.hurtpolandroid.ui.service.ProductService
import com.example.hurtpolandroid.ui.utils.HurtpolServiceGenerator
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.activity_product_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*
import java.util.logging.Logger

class ProductDetailActivity : AppCompatActivity() {

    val logger = Logger.getLogger(ProductDetailActivity::class.java.name)
    var productDetail: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        setSupportActionBar(toolbar)

        val productID = intent?.extras?.getInt(HomeActivity.PROUCT_ID_MESSAGE)
        if (productID == null) {
            Toast.makeText(this, getString(R.string.null_product_id), Toast.LENGTH_LONG).show()
            finish()
        } else {
            getProductDetail(productID)
        }
        add_product_button.setOnClickListener {
            //TODO add product to basket
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    private fun getProductDetail(productID: Int) {
        val productService = HurtpolServiceGenerator()
            .createService(ProductService::class.java)
        val context = this
        productService.getProductByID(productID)
            .enqueue(object : Callback<Product> {
                override fun onResponse(call: Call<Product>, response: Response<Product>) {
                    productDetail = response.body()
                    val productTextView = findViewById<TextView>(R.id.product_detail_text)
                    if (productDetail != null) {
                        productTextView.text = getString(
                            R.string.product_detail,
                            productDetail?.name,
                            productDetail?.description,
                            productDetail?.unitPrice
                        )
                        val imageURL = productDetail?.imageUrl
                        if (imageURL != null) {
                            val imageTmpName = UUID.randomUUID().toString()
                            Fuel.download(imageURL)
                                .fileDestination { _, _ ->
                                    File.createTempFile(imageTmpName, ".tmp")
                                }.response { result ->
                                    result.success {
                                        logger.info("Opening product image file")

                                        logger.info("Uploading product image to ImageView")
                                        val productBitMap = BitmapFactory.decodeByteArray(it, 0, it.size)
                                        runOnUiThread {
                                            val productImage = findViewById<ImageView>(R.id.product_image)
                                            productImage.setImageBitmap(productBitMap)
                                        }
                                    }
                                    result.failure {
                                        val error = it.toString()
                                        logger.warning("Download image failed $error")
                                    }
                                }

                        }
                    } else {
                        productTextView.text = getString(R.string.null_product_id)
                    }
                }

                override fun onFailure(call: Call<Product>, t: Throwable) {
                    Toast.makeText(context, getString(R.string.server_error), Toast.LENGTH_LONG).show()
                    logger.warning(t.printStackTrace().toString())

                }
            })
    }


}
