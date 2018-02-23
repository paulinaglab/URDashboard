package com.gogreenyellow.pglab.urdashboard.scanner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

/**
 * Created by Paulina on 2018-02-22.
 */
class ScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    companion object {
        const val RESULT_KEY = "scanned"
    }

    private lateinit var scannerView: ZXingScannerView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scannerView = ZXingScannerView(this)
        scannerView.setFormats(arrayListOf(BarcodeFormat.QR_CODE))
        setContentView(scannerView)
    }

    override fun onResume() {
        super.onResume()
        scannerView.setResultHandler(this)
        scannerView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        scannerView.setResultHandler(this)
        scannerView.stopCamera()
    }

    override fun handleResult(result: Result?) {
        val resultIntent = Intent()
        resultIntent.putExtra(RESULT_KEY, result?.text)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

}