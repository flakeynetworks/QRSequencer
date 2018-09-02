package uk.co.flakeynetworks.qrcodesequence.ui;


import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import uk.co.flakeynetworks.qrcodesequence.MainActivity;
import uk.co.flakeynetworks.qrcodesequence.R;

/**
 * Created by Richard Stokes on 8/28/2018.
 */

public class FragmentHome extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view =  inflater.inflate(R.layout.fragment_home, container, false);
        view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Button startScanning = view.findViewById(R.id.start_scan);
        startScanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity activity = (MainActivity) getActivity();
                activity.startScanning();
            } // end of onClick
        });
        return view;
    } // end of onCreateView
} // end of FragmentHome
