package uk.co.flakeynetworks.qrcodesequence;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import uk.co.flakeynetworks.qrcodesequence.model.ScanResult;
import uk.co.flakeynetworks.qrcodesequence.model.Sequence;
import uk.co.flakeynetworks.qrcodesequence.ui.FragmentAbout;
import uk.co.flakeynetworks.qrcodesequence.ui.FragmentHistory;
import uk.co.flakeynetworks.qrcodesequence.ui.FragmentHome;
import uk.co.flakeynetworks.qrcodesequence.ui.FragmentScanResults;
import uk.co.flakeynetworks.qrcodesequence.ui.FragmentSend;
import uk.co.flakeynetworks.qrcodesequence.ui.FragmentSequence;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Initialise Fabrio.io with Crashlytics
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);


        setContentView(R.layout.activity_main);

        // Lock to portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Setup the navigation on the the bottom
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.action_scan:
                                startScanning();
                                return true;

                            case R.id.action_about:
                                loadAbout();
                                return true;

                            case R.id.action_history:
                                loadHistory();
                                return true;

                            case R.id.action_send:
                                loadSend(null);
                                return true;

                        } // end of switch
                        return false;
                    }
                });

        // Check to see if there was an intent from another application
        if(!intentFromApplications())
            loadHomeFragment();
    } // end of onCreate


    public boolean intentFromApplications() {

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null && "text/plain".equals(type)) {

            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (sharedText != null) {

                loadSend(sharedText);
                return true;
            } // end of if
        } // end of if

        return false;
    } // end of intentFromApplications


    public void loadSend(String message) {

        FragmentSend sendFragment = new FragmentSend();

        if(message != null)
            sendFragment.setMessage(message);

        loadFragment(sendFragment, true);
    } // end of loadSend


    public void loadSequence(Sequence sequence) {

        FragmentSequence fragment = new FragmentSequence();
        fragment.setSequence(sequence);

        loadFragment(fragment, false);
    } // end of loadSequence


    public void loadHistory() {

        FragmentHistory historyFragment = new FragmentHistory();
        loadFragment(historyFragment, true);
    } // end of loadHistory


    public void startScanning() {

        Intent i = new Intent(MainActivity.this, QRSequenceScanner.class);
        startActivityForResult(i, 1);
    } // end of startScanning


    public void loadHomeFragment() {

        FragmentHome homeFragment = new FragmentHome();
        loadFragment(homeFragment, true);
    } // end of loadHomeFragment


    private void loadFragment(Fragment fragment, boolean replace) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if(!replace)
            transaction.addToBackStack(null);

        transaction.replace(R.id.fragmentPlaceHolder, fragment);

        transaction.commit();
    } // end of loadFragment


    public void loadAbout() {

        FragmentAbout about = new FragmentAbout();
        loadFragment(about, true);
    } // end of loadAbout

    public void loadScanResults(ScanResult result) {

        FragmentScanResults scanResults = new FragmentScanResults();
        scanResults.setResult(result);

        loadFragment(scanResults, false);
    } // end of loadScanResults


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            if(resultCode == RESULT_OK) {

                // Get the result and save it to file
                ScanResult result = data.getParcelableExtra("scanResult");
                boolean saved = result.save(this.getApplicationContext());
                if(!saved)
                    Snackbar.make(bottomNavigationView, "Error! Could not save this to history.", Snackbar.LENGTH_SHORT).show();

                // Vibrate to let the user know a message has been received
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    if(v != null)
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {

                    //deprecated in API 26
                    if(v != null)
                        v.vibrate(500);
                } // end of else

                // Show the results
                loadScanResults(result);
            } // end of if
        } // end of if
    } // end of onActivityResult
} // end of MainActivity
