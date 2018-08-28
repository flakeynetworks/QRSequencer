package qrcodesequence.flakeynetworks.com.qrcodesequencetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        messageText = findViewById(R.id.message_text);
        messageText.setText("Start Scanning to receive a message");

        final Button startScan = findViewById(R.id.start_scan);
        startScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, QRSequenceScanner.class);
                startActivityForResult(i, 1);
            }
        });
    } // end of onCreate


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            if(resultCode == RESULT_OK) {

                message = data.getStringExtra("message");
                messageText.setText(message);
            } // end of if
        } // end of if
    } // end of onActivityResult


    public void shareMessage() {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(sharingIntent, "Flakeynetworks QR Sequence"));
    } // end of shareMessage
} // end of MainActivity
