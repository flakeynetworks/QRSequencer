package uk.co.flakeynetworks.qrcodesequence.ui;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.flakeynetworks.qrcodesequence.R;
import uk.co.flakeynetworks.qrcodesequence.model.Sequence;

/**
 * Created by Richard Stokes on 9/6/2018.
 */

public class FragmentSequence extends Fragment {


    private Sequence sequence;
    private ImageView qrImageView;
    private TextView statisicsText;

    private int counter = 0;

    private Thread thread;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            while (true) {

                try {
                    Thread.sleep(sequence.getInterval());

                    showNextQR();
                } catch (Exception e) {
                    return;
                } // end of catch
            } // end of while
        }
    };


    public void setSequence(Sequence sequence) {

        this.sequence = sequence;
    } // end of setSequence


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view =  inflater.inflate(R.layout.fragment_sequence, container, false);
        view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        qrImageView = view.findViewById(R.id.qr);
        statisicsText = view.findViewById(R.id.statistics);

        showNextQR();

        return view;
    } // end of onCreateView


    @Override
    public void onPause() {

        super.onPause();

        if(thread != null) {
            thread.stop();
            thread = null;
        } // end of if
    } // end of onPause


    @Override
    public void onResume() {

        super.onResume();

        // Add a thread to iterate through the qr codes
        if(thread == null) {
            Thread thread = new Thread(runnable);
            thread.start();
        } // end of if
    } // end of onResume


    @Override
    public void onDestroy() {

        super.onDestroy();

        if(thread != null) {
            thread.stop();
            thread = null;
        } // end of if
    } // end of onDestroy


    private void showNextQR() {

        QRCodeWriter writer = new QRCodeWriter();

        // Check to see if we need to loop back to the beginning
        if(counter >= sequence.getNumberOfPayloads())
            counter = 0;

        try {
            JSONObject json = new JSONObject();

            // Put the message index
            json.put("m", counter);

            // Put the payload
            json.put("p", sequence.getPayload(counter));

            // Put the total number of messages
            json.put("c", sequence.getNumberOfPayloads());

            // Create the QR code
            BitMatrix bitMatrix = writer.encode(json.toString(), BarcodeFormat.QR_CODE, 500, 500);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            final Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++)
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            } // end of for

            counter++;

            // Set the QR
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    
                    qrImageView.setImageBitmap(bmp);

                    // Update the statistics text
                    statisicsText.setText(String.format("Message: %d / %d", counter, sequence.getNumberOfPayloads()));
                } // end of run
            });
        } catch (JSONException | WriterException e) {
            e.printStackTrace();
        } // end of catch
    } // end of showNextQR
} // end of FragmentSequence
