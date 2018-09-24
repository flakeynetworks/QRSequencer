package uk.co.flakeynetworks.qrcodesequence;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

    private static final int requestCode = 200;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                displayInvalidPermissions();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, requestCode);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            } // end of else
        } else {

            // Permission has already been granted
            startScanning();
        } // end of else
    } // end of onCreate

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grantResults){

        switch(permsRequestCode){

            case requestCode:

                boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if(cameraAccepted)
                    startScanning();
                else
                    displayInvalidPermissions();
                break;
        } // end of switch
    } // end of onRequestPermissionsResult


    private void displayInvalidPermissions() {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        else
            builder = new AlertDialog.Builder(this);

        builder.setTitle("Could not scan")
                .setMessage("Permission has not been granted to access the camera. Would you like to grant permission?")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(QRSequenceScanner.this,
                                new String[]{Manifest.permission.CAMERA}, requestCode);
                    } // end of onClick
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    } // end of onClick
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    } // end of displayInvalidPermissions


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

        if(mScannerView != null) {
            mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
            mScannerView.setAutoFocus(false);
            mScannerView.startCamera();          // Start camera on resume
        } // end of if
    } // end of onResume


    @Override
    public void onStop() {

        super.onStop();
        if(mScannerView != null)
            mScannerView.stopCamera();
    } // end of onStop


    @Override
    public void onPause() {

        super.onPause();

        if(mScannerView != null)
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
        } catch (JSONException e) {

            // Assume this is a genuine QR code only if we haven't started scanning a sequence
            if(messages != null)  {

                mScannerView.resumeCameraPreview(this);
                return;
            } // end of if

            // Save some of the details
            long currentTime = System.currentTimeMillis();
            scanTime = currentTime - scanStartTime;

            ScanResult result = new ScanResult(payload, currentTime, scanTime, -1);

            Intent intent = new Intent();
            intent.putExtra("scanResult", result);
            setResult(RESULT_OK, intent);
            finish();
            return;
        } // end of catch

        mScannerView.resumeCameraPreview(this);
    } // end of handleResult
} // end of QRSequenceScanner