package com.example.hurtpolandroid.ui.customer.shoppingCart

import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import com.example.hurtpolandroid.R
import com.example.hurtpolandroid.ui.model.Address
import com.example.hurtpolandroid.ui.model.ShoppingCartItem
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*
import kotlinx.android.synthetic.main.activity_shopping_cart.*
import kotlinx.android.synthetic.main.content_home.recyclerView
import org.json.JSONException
import org.json.JSONObject


class ShoppingCartActivity : AppCompatActivity() {
    lateinit var model: ShoppingCartViewModel
    private val LOAD_PAYMENT_DATA_REQUEST_CODE = 991
    private lateinit var paymentsClient: PaymentsClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_cart)

        model = ShoppingCartViewModel(this)
        val linearManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = linearManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        paymentsClient = PaymentsUtil.createPaymentsClient(this)
        possiblyShowGooglePayButton()

        btn_pay.setOnClickListener {
            it.isClickable = false
            requestPayment()
        }


        val myCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.RIGHT
        ) {
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
                c.clipRect(
                    0f, viewHolder.itemView.top.toFloat(),
                    dX, viewHolder.itemView.bottom.toFloat()
                )
                val display = windowManager.defaultDisplay
                val background = ColorDrawable(ContextCompat.getColor(this@ShoppingCartActivity, R.color.alertColor))
                background.setBounds(
                    0, viewHolder.itemView.top,
                    (viewHolder.itemView.left + dX).toInt(), viewHolder.itemView.bottom
                )
                background.draw(c)
                val icon = ContextCompat.getDrawable(this@ShoppingCartActivity, R.drawable.ic_delete_black_24dp)
                icon?.setBounds(30, viewHolder.itemView.top + 60, 110, viewHolder.itemView.top + 140)
                icon?.draw(c)
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

            }

        }

        val myHelper = ItemTouchHelper(myCallback)
        myHelper.attachToRecyclerView(recyclerView)

        model.shoppingCart.observe(this,
            Observer<List<ShoppingCartItem>> { t ->
                recyclerView.adapter = ShoppingCartAdapter(this@ShoppingCartActivity, t.orEmpty(), model)
                total_item.text = model.getTotalItem().toString()
                total_price.text = model.getTotalPriceWithCurrency()
            })
        getShoppingCart()
    }


    private fun getShoppingCart() {
        model.getShoppingCart()
    }

    private fun possiblyShowGooglePayButton() {

        val isReadyToPayJson = PaymentsUtil.isReadyToPayRequest()
        val request = IsReadyToPayRequest.fromJson(isReadyToPayJson.toString()) ?: return

        val task = paymentsClient.isReadyToPay(request)
        task.addOnCompleteListener { completedTask ->
            try {
                completedTask.getResult(ApiException::class.java)?.let(::setGooglePayAvailable)
            } catch (exception: ApiException) {
                Log.w("isReadyToPay failed", exception)
            }
        }
    }

    private fun setGooglePayAvailable(available: Boolean) {
        if (available) {
            btn_pay.visibility = View.VISIBLE
        } else {
            Toast.makeText(
                this,
                "Unfortunately, Google Pay is not available on this device",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun requestPayment() {
        val shippingCost = 0
        val price = model.getTotalPrice().plus(shippingCost)

        val paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(price.toString())
        val request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString())
        AutoResolveHelper.resolveTask(
            paymentsClient.loadPaymentData(request), this@ShoppingCartActivity, LOAD_PAYMENT_DATA_REQUEST_CODE
        )
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK ->
                        data?.let { intent ->
                            PaymentData.getFromIntent(intent)?.let(::handlePaymentSuccess)
                        }
                    Activity.RESULT_CANCELED -> {
                    }
                    AutoResolveHelper.RESULT_ERROR -> {
                        AutoResolveHelper.getStatusFromIntent(data)?.let {
                            handleError(it.statusCode)
                        }
                    }
                }
                btn_pay.isClickable = true
            }
        }
    }

    private fun handlePaymentSuccess(paymentData: PaymentData) {
        val paymentInformation = paymentData.toJson() ?: return

        try {
            val paymentMethodData = JSONObject(paymentInformation).getJSONObject("paymentMethodData")
            val billingAddress = paymentMethodData.getJSONObject("info").getJSONObject("billingAddress")
            val deliveryAddress = Address(
                billingAddress.getString("locality"),
                billingAddress.getString("postalCode"),
                billingAddress.getString("address1")
            )

            val googlePaymentToken = paymentMethodData.getJSONObject("tokenizationData").getString("token")
            model.payForOrder(deliveryAddress, googlePaymentToken)

        } catch (e: JSONException) {
            Log.e("handlePaymentSuccess", "Error: $e")
        }
    }

    private fun handleError(statusCode: Int) {
        Log.w("loadPaymentData failed", String.format("Error code: %d", statusCode))
    }

}
