package uk.co.flakeynetworks.qrcodesequence.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import uk.co.flakeynetworks.qrcodesequence.MainActivity;
import uk.co.flakeynetworks.qrcodesequence.R;
import uk.co.flakeynetworks.qrcodesequence.model.Sequence;

/**
 * Created by Richard Stokes on 9/6/2018.
 */

public class FragmentSend extends Fragment {


    private String message;
    private EditText messageField;

    private SeekBar maxPayloadSize;
    private TextView payloadThumbView;

    private SeekBar interval;
    private TextView intervalThumb;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view;
        view =  inflater.inflate(R.layout.fragment_send, container, false);
        view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        messageField = view.findViewById(R.id.message);


        // Setup the max payload UI
        maxPayloadSize = view.findViewById(R.id.maxPayloadSize);
        payloadThumbView = view.findViewById(R.id.maxPayloadThumb);

        maxPayloadSize.setMax(getResources().getInteger(R.integer.maxPayloadSize));
        maxPayloadSize.setProgress(getResources().getInteger(R.integer.payloadSizeDefault));

        payloadThumbView.setText(Integer.toString(maxPayloadSize.getProgress()));
        maxPayloadSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                payloadThumbView.setText(Integer.toString(seekBar.getProgress()));
            } // end of onProgressChanged

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            } // end of onStartTrackingTouch

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            } // end of onStopTrackingTouch
        });


        // Setup the interval UI
        interval = view.findViewById(R.id.sequenceInterval);
        intervalThumb = view.findViewById(R.id.intervalThumb);

        interval.setMax(getResources().getInteger(R.integer.maxInterval));
        interval.setProgress(getResources().getInteger(R.integer.intervalDefault));


        intervalThumb.setText(Integer.toString(maxPayloadSize.getProgress()));
        interval.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                intervalThumb.setText(Integer.toString(seekBar.getProgress()));
            } // end of onProgressChanged

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            } // end of onStartTrackingTouch

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            } // end of onStopTrackingTouch
        });


        // Setup the display button
        Button btnDisplay = view.findViewById(R.id.display);
        btnDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = messageField.getText().toString();
                int maxPayload = maxPayloadSize.getProgress();
                int intervalTime = interval.getProgress();

                if(maxPayload < 1) maxPayload = 1;
                if(intervalTime < 1) intervalTime = 1;

                Sequence sequence = new Sequence(message, maxPayload, intervalTime);

                // Start the layout
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.loadSequence(sequence);
            } // end of onClick
        });

        // Set the message
        if(message != null) messageField.setText(message);

        return view;
    } // end of onCreateView



    public void setMessage(String message) {

        this.message = message;
    } // end of setMessage
} // end of FragmentSend
