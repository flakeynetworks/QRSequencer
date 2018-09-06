package uk.co.flakeynetworks.qrcodesequence;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import uk.co.flakeynetworks.qrcodesequence.model.ScanResult;

/**
 * Created by Richard Stokes on 8/27/2018.
 */

public class QRSequenceScanner extends Activity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private String[] messages;
    private long scanStartTime;
    private long scanTime;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        startScanning();
    } // end of onCreate


    private void startScanning() {

        scanStartTime = System.currentTimeMillis();

        // Load in the custom layout
        setContentView(R.layout.custom_scanning_layout);

        // Get the custom UI elements we will need later
        progressBar = findViewById(R.id.progressBar);

        // Programmatically initialize the scanner view
        mScannerView = new ZXingScannerView(this);

        // Add the scanner view to the custom view
        ZXingScannerView zscanner = findViewById(R.id.zxscan);
        zscanner.addView(mScannerView);


        mScannerView.setLaserEnabled(false);

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
            } // end of onClick
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

    private int getNumberOfPayloadsReceived() {

        int counter = 0;
        for(String message: messages) {
            if (message != null)
                counter++;
        } // end of for

        return counter;
    } // end of getNumberOfPayloadsReceived


    @Override
    public void handleResult(Result rawResult) {

        // Do something with the result here
        String payload = rawResult.getText();

        try {

            JSONObject payloadJSON = new JSONObject(payload);

            int messageNumber = payloadJSON.getInt("m");
            int totalMessages = payloadJSON.getInt("c");
            String payloadMessage = payloadJSON.getString("p");

            // Found the total number of messages we should received. Update everything
            if(messages == null || messages.length != totalMessages) {

                messages = new String[totalMessages];
                progressBar.setMax(totalMessages);
            } // end of if

            messages[messageNumber] = payloadMessage;

            // Update the UI
            progressBar.setProgress(getNumberOfPayloadsReceived());


            if(hasMessagesBeenReceived()) {

                // Save some of the details
                long currentTime = System.currentTimeMillis();
                scanTime = currentTime - scanStartTime;
                String entireMessage = getMessage();

                ScanResult result = new ScanResult(entireMessage, currentTime, scanTime, messages.length);

                Intent intent = new Intent();
                intent.putExtra("scanResult", result);
                setResult(RESULT_OK, intent);
                finish();
                return;
            } // end of if
        } catch (JSONException e) { } // end of catch

        mScannerView.resumeCameraPreview(this);
    } // end of handleResult
} // end of QRSequenceScanner