package uk.co.flakeynetworks.qrcodesequence;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;


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
                                // TODO
                                return true;
                        } // end of switch
                        return false;
                    }
                });

        loadHomeFragment();
    } // end of onCreate


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

    public void loadScanResults(String message) {

        FragmentScanResults scanResults = new FragmentScanResults();

        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        scanResults.setArguments(bundle);

        loadFragment(scanResults);
    } // end of loadScanResults


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            if(resultCode == RESULT_OK) {

                String message = data.getStringExtra("message");

                // Show the results
                loadScanResults(message);
            } // end of if
        } // end of if
    } // end of onActivityResult


    public void shareMessage(String message) {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(sharingIntent, "Flakeynetworks QR Sequence"));
    } // end of shareMessage
} // end of MainActivity
