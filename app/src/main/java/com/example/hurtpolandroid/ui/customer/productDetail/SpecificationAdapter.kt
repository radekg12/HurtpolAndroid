package com.example.hurtpolandroid.ui.customer.productDetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hurtpolandroid.R
import com.example.hurtpolandroid.ui.model.Specification

class SpecificationAdapter(val context: Context, val productList: List<Specification>) :
    RecyclerView.Adapter<SpecificationAdapter.SpecificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecificationViewHolder {
        //inflating and returning our view holder
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.layout_specification, null)
        return SpecificationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: SpecificationViewHolder, position: Int) {
        //getting the product of the specified position
        val product = productList.get(position)

        //binding the data with the viewholder views
        holder.name.text = product.name
        holder.value.text = product.value

    }

    class SpecificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name = itemView.findViewById<TextView>(R.id.spec_name)
        var value = itemView.findViewById<TextView>(R.id.spec_value)
    }
}