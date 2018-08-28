package uk.co.flakeynetworks.qrcodesequence;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Richard Stokes on 8/28/2018.
 */

public class FragmentScanResults extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view =  inflater.inflate(R.layout.fragment_scan_results, container, false);
        view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        // Get the message to display
        Bundle bundle = getArguments();

        String message = "";
        if(bundle.containsKey("message"))
            message = bundle.getString("message");

        TextView messageView = view.findViewById(R.id.message_text);
        messageView.setText(message);

        return view;
    } // end of onCreateView
} // end of FragmentScanResults
