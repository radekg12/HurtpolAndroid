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
import com.example.hurtpolandroid.ui.model.Product
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

        val operationType = intent?.extras?.getSerializable("Operation")
        if (operationType == OperationType.TAKE) {
            imageView.setImageDrawable(resources.getDrawable(R.drawable.take))
            operation_name.text = getString(R.string.take_title)
        } else {
            imageView.setImageDrawable(resources.getDrawable(R.drawable.put))
            operation_name.text = getString(R.string.put_title)
        }

        btn_confirm.setOnClickListener {
            if (validate()) {
                val id = product_code.text.toString().toLong()
                val quantity = product_count.text.toString().toInt()
                btn_confirm.isEnabled = false
                if (operationType == OperationType.TAKE) {
                    scannerViewModel.take(id, quantity).enqueue(this)
                } else {
                    scannerViewModel.put(id, quantity).enqueue(this)
                }
            }
        }

        product_code.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                if (event.action == MotionEvent.ACTION_UP) {
                    val textView = v as TextView
                    if (event.x >= textView.width - textView.compoundPaddingEnd) {
                        scan()
                        return true
                    }
                }
                return false
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Anulowano", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Zeskanowano: " + result.contents, Toast.LENGTH_LONG).show()
                product_code.setText(result.contents.toString(), TextView.BufferType.EDITABLE)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun scan() {
        val scanner = IntentIntegrator(this)
        scanner.setBeepEnabled(true)
        scanner.initiateScan()
    }

    override fun onFailure(call: Call<Product>, t: Throwable) {
        btn_confirm.isEnabled = true
        Toast.makeText(baseContext, "Błąd serwera", Toast.LENGTH_LONG).show()
    }

    override fun onResponse(call: Call<Product>, response: Response<Product>) {
        btn_confirm.isEnabled = true
        if (response.isSuccessful) {
            Toast.makeText(baseContext, "Operacja zakończona pomyślnie", Toast.LENGTH_LONG).show()
            finish()
        } else {
            Toast.makeText(baseContext, "Operacja zakończona niepowodzeniem", Toast.LENGTH_LONG).show()
        }
    }

    private fun validate(): Boolean {
        var valid = true

        val id = product_code.text.toString()
        val quantity = product_count.text.toString()

        if (id.isEmpty() || !TextUtils.isDigitsOnly(id)) {
            product_code.error = "Błędna wartość"
            valid = false
        } else {
            product_code.error = null
        }

        if (quantity.isEmpty() || !TextUtils.isDigitsOnly(quantity)) {
            product_count.error = "Błędna wartość"
            valid = false
        } else {
            product_count.error = null
        }

        return valid
    }
}
