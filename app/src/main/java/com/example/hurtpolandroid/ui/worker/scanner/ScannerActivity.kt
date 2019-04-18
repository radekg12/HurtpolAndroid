package com.example.hurtpolandroid.ui.worker.scanner

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hurtpolandroid.ui.worker.OperationType
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_scanner.*
import android.view.MotionEvent
import android.view.View
import com.example.hurtpolandroid.R


class ScannerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        val operationType = intent.extras.getSerializable("Operation")
        if (operationType == OperationType.TAKE) {
            imageView.setImageDrawable(resources.getDrawable(com.example.hurtpolandroid.R.drawable.take))
            operation_name.text = getString(R.string.take_title)
        } else {
            imageView.setImageDrawable(resources.getDrawable(com.example.hurtpolandroid.R.drawable.put))
            operation_name.text = getString(R.string.put_title)
        }

        btn_confirm.setOnClickListener {
            //TODO wyslij requesta
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

    fun scan(){
        val scanner = IntentIntegrator(this)
        scanner.setBeepEnabled(false)
        scanner.initiateScan()
    }
}
