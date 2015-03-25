package com.nightwind.museum;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;


public class ScannerActivity extends ActionBarActivity implements ZBarScannerView.ResultHandler {

    public static final String TAG = ScannerActivity.class.getSimpleName();
    public static final String RESULT_DATA_QR_CODE = "qr_code";
    private ZBarScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_scanner);

        mScannerView = new ZBarScannerView(this);

        ArrayList<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.QRCODE);
        mScannerView.setFormats(formats);

        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register ourselves as a handler for scan results.
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result result) {
        Log.v(TAG, result.getContents()); // Prints scan results
        Intent data = new Intent();
        data.putExtra(RESULT_DATA_QR_CODE, result.getContents());
        setResult(RESULT_OK, data);
        finish();
    }
}
