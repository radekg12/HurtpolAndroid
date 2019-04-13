package com.example.hurtpolandroid.ui.worker.scanner

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hurtpolandroid.R
import com.example.hurtpolandroid.ui.worker.OperationType
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_scanner.*

class ScannerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        val operationType = intent.extras.getSerializable("Operation")
        if (operationType == OperationType.TAKE) {
            imageView.setImageDrawable(resources.getDrawable(R.drawable.take))
            opetation_name.text = getString(R.string.take_title)
        } else {
            imageView.setImageDrawable(resources.getDrawable(R.drawable.put))
            opetation_name.text = getString(R.string.put_title)
        }

        btn_scan.setOnClickListener {
            val scanner = IntentIntegrator(this)
            scanner.setBeepEnabled(false)
            scanner.initiateScan()
        }

        btn_confirm.setOnClickListener {
            //TODO wyslij requesta
        }
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
}
