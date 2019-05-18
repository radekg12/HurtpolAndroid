package com.example.hurtpolandroid.ui.customer.shoppingCart

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import com.example.hurtpolandroid.R
import com.example.hurtpolandroid.ui.model.ShoppingCartItem
import kotlinx.android.synthetic.main.activity_shopping_cart.*
import kotlinx.android.synthetic.main.content_home.recyclerView


class ShoppingCartActivity : AppCompatActivity() {
    lateinit var model: ShoppingCartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_cart)

        model = ShoppingCartViewModel(this)
        val linearManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = linearManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))


        val myCallback = object: ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                model.removeProduct(viewHolder.adapterPosition)
            }


            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                c.clipRect(0f, viewHolder.itemView.top.toFloat(),
                    dX, viewHolder.itemView.bottom.toFloat())
                val display = windowManager.defaultDisplay
                var background: ColorDrawable
                background= ColorDrawable(ContextCompat.getColor(this@ShoppingCartActivity,R.color.alertColor))
                background.setBounds(0, viewHolder.itemView.getTop(),
                    (viewHolder.itemView.getLeft() + dX).toInt(),  viewHolder.itemView.getBottom());
                background.draw(c);
                val icon = ContextCompat.getDrawable(this@ShoppingCartActivity, R.drawable.ic_delete_black_24dp);
                icon?.setBounds(30, viewHolder.itemView.getTop()+60, 110, viewHolder.itemView.getTop()+140);
                icon?.draw(c);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

            }

            // More code here

        }

        val myHelper = ItemTouchHelper(myCallback)
        myHelper.attachToRecyclerView(recyclerView)

        model.shoppingCart.observe(this, object : Observer<List<ShoppingCartItem>> {
            override fun onChanged(t: List<ShoppingCartItem>?) {
                recyclerView.adapter = ShoppingCartAdapter(this@ShoppingCartActivity, t.orEmpty(), model)
                total_item.text = model.getTotalItem().toString()
                total_price.text = model.getTotalPriceWithCurrency()
            }

        })


        getShoppingCart()
    }


    private fun getShoppingCart() {
        model.getShoppingCart()
    }
}
