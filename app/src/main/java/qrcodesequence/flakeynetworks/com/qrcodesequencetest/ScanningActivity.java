package qrcodesequence.flakeynetworks.com.qrcodesequencetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanningActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private String[] messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startScanning();
    }

    @Override
    public void onResume() {

        super.onResume();

         mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
         mScannerView.startCamera();          // Start camera on resume
    } // end of onResumer


    @Override
    public void onPause() {
        super.onPause();

         mScannerView.stopCamera();           // Stop camera on pause
    } // end of onPause

    public void startScanning() {

        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view

        mScannerView.setFlash(false);
        mScannerView.setAutoFocus(true);
        mScannerView.setLaserEnabled(false);

        List<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.QR_CODE);
        mScannerView.setFormats(formats);

        setContentView(mScannerView);                // Set the scanner view as the content view
        mScannerView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                mScannerView.setCameraDistance(1000);
                mScannerView.findFocus();
            }
        });
    } // end of startScanning

    public boolean hasMessagesBeenReceived() {

        if(messages == null) return false;

        for(int i = 0; i < messages.length; i++) {

            if(messages[i] == null) return false;
        } // end of for

        return true;
    } // end of hasMessagesBeenReceived


    @Override
    public void handleResult(Result rawResult) {

        // Do something with the result here
        String payload = rawResult.getText();

        try {

            JSONObject payloadJSON = new JSONObject(payload);

            Log.w("payload", payload);

            int messageNumber = payloadJSON.getInt("m");
            int totalMessages = payloadJSON.getInt("c");
            String payloadMessage = payloadJSON.getString("p");

            if(messages == null) messages = new String[totalMessages];

            messages[messageNumber] = payloadMessage;

            if(hasMessagesBeenReceived()) {

                Toast toast = Toast.makeText(this, "All Messages Received", Toast.LENGTH_SHORT);
                toast.show();

                return;
            } // end of if
        } catch (JSONException e) {
            Log.w("payload", "sdfsdf", e);
        } // end of catch


        mScannerView.resumeCameraPreview(this);
    }
}
