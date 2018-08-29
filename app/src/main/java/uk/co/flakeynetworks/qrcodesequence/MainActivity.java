package uk.co.flakeynetworks.qrcodesequence;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.view.MenuItem;

// TODO change the name to QR Sequencer

public class MainActivity extends Activity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

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
                        } // end of switch
                        return false;
                    }
                });

        loadHomeFragment();
    } // end of onCreate


    public void loadHistory() {

        FragmentHistory historyFragment = new FragmentHistory();
        loadFragment(historyFragment);
    } // end of loadHistory


    public void startScanning() {

        Intent i = new Intent(MainActivity.this, QRSequenceScanner.class);
        startActivityForResult(i, 1);
    } // end of startScanning


    public void loadHomeFragment() {

        FragmentHome homeFragment = new FragmentHome();
        loadFragment(homeFragment);
    } // end of loadHomeFragment


    private void loadFragment(Fragment fragment) {

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragmentPlaceHolder, fragment);
        transaction.commit();
    } // end of loadFragment


    public void loadAbout() {

        FragmentAbout about = new FragmentAbout();
        loadFragment(about);
    } // end of loadAbout

    public void loadScanResults(ScanResult result) {

        FragmentScanResults scanResults = new FragmentScanResults();
        scanResults.setResult(result);

        loadFragment(scanResults);
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
