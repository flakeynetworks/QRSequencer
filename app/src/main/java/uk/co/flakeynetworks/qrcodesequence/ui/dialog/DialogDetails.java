package uk.co.flakeynetworks.qrcodesequence.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.TextView;

import uk.co.flakeynetworks.qrcodesequence.R;
import uk.co.flakeynetworks.qrcodesequence.model.ScanResult;

/**
 * Created by Richard Stokes on 9/7/2018.
 */

public class DialogDetails extends Dialog {


    private ScanResult result;


    public DialogDetails(@NonNull Context context, ScanResult result) {

        super(context);

        this.result = result;
    } // end of constructor

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_advanced_details);


        TextView scanDate = findViewById(R.id.scanDate);
        TextView scanTime = findViewById(R.id.scanTime);
        TextView noOfPayloads = findViewById(R.id.numberOfPayloads);
        TextView messageLength = findViewById(R.id.messageLength);

        // Set the details
        scanDate.setText(result.getDateAndTimeTaken());
        scanTime.setText(result.getScanTimeFormatted());

        if(result.getNumberOfPayloads() <= 0)
            noOfPayloads.setText("Not a QR sequence");
        else
            noOfPayloads.setText(Integer.toString(result.getNumberOfPayloads()));

        messageLength.setText(Integer.toString(result.getMessageLength()) + " Characters");

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setCanceledOnTouchOutside(true);
    } // end of onCreate
} // end of DialogDetails
