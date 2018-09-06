package uk.co.flakeynetworks.qrcodesequence.ui;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.co.flakeynetworks.qrcodesequence.R;

/**
 * Created by Richard Stokes on 9/6/2018.
 */

public class FragmentSend extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view =  inflater.inflate(R.layout.fragment_send, container, false);
        view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return view;
    } // end of onCreateView
} // end of FragmentSend
