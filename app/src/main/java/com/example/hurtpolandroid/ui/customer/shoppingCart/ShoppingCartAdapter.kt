package com.example.hurtpolandroid.ui.customer.shoppingCart

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
import com.example.hurtpolandroid.ui.customer.home.HomeActivity
import com.example.hurtpolandroid.ui.customer.productDetail.ProductDetailActivity
import com.example.hurtpolandroid.ui.model.ShoppingCartItem
import com.example.hurtpolandroid.ui.service.ShoppingCartService
import com.example.hurtpolandroid.ui.utils.HurtpolServiceGenerator
import com.google.android.material.button.MaterialButton
import java.text.NumberFormat


class ShoppingCartAdapter(
    private val context: Context,
    private val shoppingCart: List<ShoppingCartItem>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<ShoppingCartAdapter.ProductViewHolder>() {

    interface OnItemClickListener {
        fun onBtnPlusClick(item: ShoppingCartItem)
        fun onBtnMinusClick(item: ShoppingCartItem)
    }

    val service = HurtpolServiceGenerator().createServiceWithToken(ShoppingCartService::class.java, context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        //inflating and returning our view holder
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.layout_cart_item, null)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return shoppingCart.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        //getting the product of the specified position
        val cart = shoppingCart[position]

        //binding the data with the viewholder views
        holder.textViewTitle.text = cart.product.name
        val format = NumberFormat.getCurrencyInstance()
        val currency = format.format(cart.product.unitPrice.div(100))
        holder.textViewPrice.text = currency
        holder.quantity.text = cart.quantity.toString()

        Glide.with(context)
            .load(cart.product.imageUrl)
            .into(holder.imageView)

        if (cart.quantity == 1)
            holder.minus.isEnabled = false

//        holder.itemView.setOnClickListener {
//            productOnClick(cart.product.id)
//        }

        holder.plus.setOnClickListener {
            listener.onBtnPlusClick(cart)
        }

        holder.minus.setOnClickListener {
            if (cart.quantity > 1)
                listener.onBtnMinusClick(cart)
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
        var textViewPrice: TextView = itemView.findViewById(R.id.textViewPrice)
        var imageView: ImageView = itemView.findViewById(R.id.imageView)
        var quantity: TextView = itemView.findViewById(R.id.product_quantity)

        var minus: MaterialButton = itemView.findViewById(R.id.minus_btn)
        var plus: MaterialButton = itemView.findViewById(R.id.plus_btn)
    }
}