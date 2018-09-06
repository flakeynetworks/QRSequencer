package uk.co.flakeynetworks.qrcodesequence.ui;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import uk.co.flakeynetworks.qrcodesequence.R;

/**
 * Created by richard on 8/28/2018.
 */

public class FragmentAbout extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view =  inflater.inflate(R.layout.fragment_about, container, false);
        view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WebView webview = view.findViewById(R.id.aboutPage);
        webview.loadUrl("https://flakeynetworks.co.uk/qr-sequencing/");

        return view;
    } // end of onCreateView
} // end of FragmentAbout
