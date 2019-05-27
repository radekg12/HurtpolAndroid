package com.example.hurtpolandroid.ui.customer.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hurtpolandroid.R
import com.example.hurtpolandroid.ui.customer.productDetail.ProductDetailActivity
import com.example.hurtpolandroid.ui.model.Product
import java.text.NumberFormat


class ProductAdapter(val context: Context, private val productList: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        //inflating and returning our view holder
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.layout_product, null)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        //getting the product of the specified position
        val product = productList[position]

        //binding the data with the viewholder views
        holder.textViewTitle.text = product.name
        holder.textViewShortDesc.text = product.description
        val format = NumberFormat.getCurrencyInstance()
        val currency = format.format(product.unitPrice.div(100))
        holder.textViewPrice.text = currency

        Glide.with(context)
            .load(product.imageUrl)
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            productOnClick(product.id)
        }

    }

    private fun productOnClick(productID: Int) {
        val intent = Intent(context, ProductDetailActivity::class.java).apply {
            putExtra(HomeActivity.PRODUCT_ID_MESSAGE, productID)
        }
        context.startActivity(intent)
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        var textViewShortDesc: TextView = itemView.findViewById(R.id.textViewShortDesc)
        var textViewPrice: TextView = itemView.findViewById(R.id.textViewPrice)
        var imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}