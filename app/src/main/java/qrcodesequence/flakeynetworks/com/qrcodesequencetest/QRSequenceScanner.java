package qrcodesequence.flakeynetworks.com.qrcodesequencetest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Richard Stokes on 8/27/2018.
 */

public class QRSequenceScanner extends Activity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private String[] messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        startScanning();
    } // end of onCreate


    private void startScanning() {

        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        mScannerView.setLaserEnabled(false);
        setContentView(mScannerView);                // Set the scanner view as the content view

        List<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.QR_CODE);
        mScannerView.setFormats(formats);
        mScannerView.setFlash(false);
        mScannerView.setAutoFocus(true);

        mScannerView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                mScannerView.setCameraDistance(1000);
                mScannerView.findFocus();
            }
        });
    } // end of startScanning


    @Override
    public void onResume() {

        super.onResume();

        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    } // end of onResume


    @Override
    public void onStop() {

        super.onStop();
        mScannerView.stopCamera();
    } // end of onStop


    @Override
    public void onPause() {

        super.onPause();

        mScannerView.stopCamera();           // Stop camera on pause
    } // end of onPause


    private boolean hasMessagesBeenReceived() {

        if(messages == null) return false;

        for(String message: messages) {

            if(message == null) return false;
        } // end of for

        return true;
    } // end of hasMessagesBeenReceived


    private String getMessage() {

        StringBuilder builder = new StringBuilder();

        for(String message: messages)
            builder.append(message);

        return builder.toString();
    } // end of getMessage


    @Override
    public void handleResult(Result rawResult) {

        // Do something with the result here
        String payload = rawResult.getText();

        try {

            JSONObject payloadJSON = new JSONObject(payload);

            int messageNumber = payloadJSON.getInt("m");
            int totalMessages = payloadJSON.getInt("c");
            String payloadMessage = payloadJSON.getString("p");

            if(messages == null || messages.length != totalMessages) messages = new String[totalMessages];

            messages[messageNumber] = payloadMessage;

            if(hasMessagesBeenReceived()) {

                Intent intent = new Intent();
                intent.putExtra("message", getMessage());
                setResult(RESULT_OK, intent);
                finish();
                return;
            } // end of if
        } catch (JSONException e) { } // end of catch

        mScannerView.resumeCameraPreview(this);
    } // end of handleResult
} // end of QRSequenceScanner