package com.example.hurtpolandroid.ui.worker.scanner

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hurtpolandroid.R
import com.example.hurtpolandroid.data.model.Product
import com.example.hurtpolandroid.ui.worker.OperationType
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_scanner.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ScannerActivity : AppCompatActivity(), Callback<Product> {

    private lateinit var scannerViewModel: ScannerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
        scannerViewModel = ScannerViewModel(this)
        setOperationTypeIcon()
        btn_confirm.setOnClickListener {
            onClickConfirmButton()
        }

        product_code.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                if (event.action == MotionEvent.ACTION_UP) {
                    val textView = v as TextView
                    if (event.x >= textView.width - textView.compoundPaddingEnd) {
                        startScan()
                        return true
                    }
                }
                return false
            }
        })
    }

    private fun onClickConfirmButton() {
        if (validateForm()) {
            val id = product_code.text.toString().toInt()
            val quantity = product_count.text.toString().toInt()
            btn_confirm.isEnabled = false
            if (getOperationType() == OperationType.TAKE.name) {
                scannerViewModel.take(id, quantity).enqueue(this)
            } else {
                scannerViewModel.put(id, quantity).enqueue(this)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, getString(R.string.canceled), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, getString(R.string.scanned) + result.contents, Toast.LENGTH_LONG).show()
                product_code.setText(result.contents.toString(), TextView.BufferType.EDITABLE)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun startScan() {
        val scanner = IntentIntegrator(this)
        scanner.setBeepEnabled(true)
        scanner.initiateScan()
    }

    override fun onFailure(call: Call<Product>, t: Throwable) {
        btn_confirm.isEnabled = true
        Toast.makeText(
            this@ScannerActivity,
            getString(R.string.server_error),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onResponse(call: Call<Product>, response: Response<Product>) {
        btn_confirm.isEnabled = true
        if (response.isSuccessful) {
            Toast.makeText(baseContext, getString(R.string.operation_completed), Toast.LENGTH_LONG).show()
            finish()
        } else {
            Toast.makeText(baseContext, getString(R.string.operationFailed), Toast.LENGTH_LONG).show()
        }
    }

    private fun setOperationTypeIcon() {
        val operationType = getOperationType()
        if (operationType == OperationType.TAKE.name) {
            imageView.setImageDrawable(getDrawable(R.drawable.take))
            operation_name.text = getString(R.string.take_title)
        } else {
            imageView.setImageDrawable(getDrawable(R.drawable.put))
            operation_name.text = getString(R.string.put_title)
        }
    }

    private fun getOperationType(): String {
        return intent?.extras?.getSerializable("Operation").toString()
    }

    private fun validateForm(): Boolean {
        val productCode = product_code.text.toString()
        val quantity = product_count.text.toString()

        return validateProductCode(productCode) && validateQuantity(quantity)
    }

    private fun validateQuantity(quantity: String): Boolean {
        return if (quantity.isEmpty() || !TextUtils.isDigitsOnly(quantity)) {
            product_count.error = getString(R.string.invalid_value)
            false
        } else {
            product_count.error = null
            true
        }
    }

    private fun validateProductCode(productCode: String): Boolean {
        return if (productCode.isEmpty() || !TextUtils.isDigitsOnly(productCode)) {
            product_code.error = getString(R.string.invalid_value)
            false
        } else {
            product_code.error = null
            true
        }
    }
}
