package uk.co.flakeynetworks.qrcodesequence.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import uk.co.flakeynetworks.qrcodesequence.R;
import uk.co.flakeynetworks.qrcodesequence.model.ScanResult;
import uk.co.flakeynetworks.qrcodesequence.ui.dialog.DialogDetails;

/**
 * Created by Richard Stokes on 8/28/2018.
 */

public class FragmentScanResults extends Fragment {

    private ScanResult scanResult;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view =  inflater.inflate(R.layout.fragment_scan_results, container, false);
        view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        // Set the message
        TextView messageView = view.findViewById(R.id.message_text);
        messageView.setText(scanResult.getMessage());


        LinearLayout btnShare = view.findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareMessage(scanResult.getMessage());
            } // end of onClick
        });

        LinearLayout btnInfo = view.findViewById(R.id.btnInfo);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogDetails details = new DialogDetails(getActivity(), scanResult);
                details.show();
            } // end of onClick
        });

        return view;
    } // end of onCreateView


    public void setResult(ScanResult result) { this.scanResult = result; } // end of setResult


    public void shareMessage(String message) {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Scan Result");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(sharingIntent, "Flakeynetworks QR Sequence"));
    } // end of shareMessage
} // end of FragmentScanResults
