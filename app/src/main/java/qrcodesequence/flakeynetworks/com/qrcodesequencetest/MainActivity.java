package qrcodesequence.flakeynetworks.com.qrcodesequencetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity  {


    private MainActivity reference = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView text = (TextView) findViewById(R.id.message_text);
        text.setText("Start Scanning to receive a message");

        final Button startScan = (Button) findViewById(R.id.start_scan);
        startScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(reference, ScanningActivity.class);
                startActivity(intent);
            }
        });
    } // end of onCreate
}
