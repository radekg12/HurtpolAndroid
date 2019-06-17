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
import com.example.hurtpolandroid.data.model.Product
import com.example.hurtpolandroid.data.model.Specification
import com.example.hurtpolandroid.ui.customer.home.HomeActivity
import com.example.hurtpolandroid.ui.customer.viewmodels.ProductDetailViewModel
import com.example.hurtpolandroid.utils.InjectionUtils
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.content_product_detail.*
import java.text.NumberFormat
import java.util.logging.Logger

class ProductDetailActivity : AppCompatActivity() {

    private val logger: Logger = Logger.getLogger(ProductDetailActivity::class.java.name)
    lateinit var model: ProductDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        setSupportActionBar(toolbar)
        initSpecificationView()

        val productID = intent?.extras!!.getInt(HomeActivity.PRODUCT_ID_MESSAGE)
        model = InjectionUtils.provideProductDetailViewModelFactory(this, productID)
            .create(ProductDetailViewModel::class.java)
        getProductDetail()
        add_product_button.setOnClickListener {
            model.addProductToShoppingCart(productID)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        observeShoppingCart()

    }

    private fun observeShoppingCart() {
        model.shoppingCartResponse.observe(this,
            Observer<Product> {
                Toast.makeText(
                    this@ProductDetailActivity,
                    getString(R.string.addedToCart),
                    Toast.LENGTH_LONG
                ).show()
            })
    }

    private fun initSpecificationView() {
        val linearManager = LinearLayoutManager(this)
        specification.setHasFixedSize(true)
        specification.layoutManager = linearManager
        specification.itemAnimator = DefaultItemAnimator()
        specification.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        ViewCompat.setNestedScrollingEnabled(specification, false)
    }

    private fun getProductDetail() {
        try {
            model.getProductDetail().observe(this, Observer<Product> { productDetail ->
                if (productDetail != null)
                    setProductDetail(productDetail)
                else
                    product_desc.text = getString(R.string.null_product_id)
                loadingProduct.visibility = View.GONE
            })

            model.getProductSpecification().observe(this, Observer<List<Specification>> {
                specification_sec.visibility = View.VISIBLE
                specification.adapter =
                    SpecificationAdapter(this@ProductDetailActivity, it)
            })

        } catch (ex: Exception) {
            logger.warning(ex.toString())
            Toast.makeText(
                this@ProductDetailActivity,
                getString(R.string.server_error),
                Toast.LENGTH_LONG
            ).show()
        }

    }

    private fun setProductDetail(productDetail: Product) {
        toolbar.title = productDetail.name
        product_name.text = productDetail.name
        product_desc.text = productDetail.description
        val format = NumberFormat.getCurrencyInstance()
        val currency = format.format(productDetail.unitPrice.div(100))
        product_price.text = currency
        Glide.with(this@ProductDetailActivity)
            .load(productDetail.imageUrl)
            .into(product_image)
    }
}
