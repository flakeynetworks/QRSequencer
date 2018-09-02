package uk.co.flakeynetworks.qrcodesequence.model;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.co.flakeynetworks.qrcodesequence.R;

/**
 * Created by Richard Stokes on 8/29/2018.
 */

public class ScanHistory {


    private List<ScanResult> history = new ArrayList<>();


    public void refreshList(Context context) {

        history.clear();

        File directory = new File(context.getFilesDir(), context.getResources().getString(R.string.historyFolder));
        if(!directory.exists())
            directory.mkdirs();

        File[] files = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".json");
            } // end of accept
        });


        // Covert these into ScanResult objects
        for(File file: files) {

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));

                StringBuilder jsonBuidler = new StringBuilder();
                String st;
                while ((st = br.readLine()) != null)
                    jsonBuidler.append(st).append("\n");

                ScanResult scanResult = ScanResult.fromJSON(jsonBuidler.toString());
                scanResult.setFile(file);

                history.add(scanResult);
            } catch (IOException ignore) { } // end of catch
        } // end of for
    } // end of refreshList


    public List<ScanResult> getResults() { return history; } // end of getResults

    public boolean delete(ScanResult result) {

        // Delete the file
        if(result.deleteFile()) {

            // Remove the result from the history list
            history.remove(result);
            return true;
        } // end of if

        return false;
    } // end of delete
} // end of ScanHistory
