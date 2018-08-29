package uk.co.flakeynetworks.qrcodesequence;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Richard Stokes on 8/29/2018.
 */

public class FragmentHistory extends Fragment {


    private ScanHistory history = new ScanHistory();
    private RefreshHistoryTask refreshHistoryTask;
    private ArrayAdapter<ScanResult> historyListAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view =  inflater.inflate(R.layout.fragment_history, container, false);
        view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        refreshHistoryTask = new RefreshHistoryTask();
        refreshHistoryTask.execute((Void) null);

        return view;
    } // end of onCreateView


    private void updateList() {

        List<ScanResult> results = history.getResults();
        Collections.sort(results);

        ListView historyList = getView().findViewById(R.id.historyList);
        historyListAdapter = new HistoryItemAdapter(getActivity(), R.layout.list_history_item, results);

        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                ScanResult result = historyListAdapter.getItem(position);

                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.loadScanResults(result);
            } // end of onItemClick
        });

        historyList.setAdapter(historyListAdapter);
    } // end of updateList


    class RefreshHistoryTask extends AsyncTask<Void, Void, Boolean> {

        protected Boolean doInBackground(Void... urls) {

            history.refreshList(getActivity());
            return true;
        } // end of doInBackground

        protected void onPostExecute(Boolean success) {

            if(success)
                updateList();
        } // onPostExecute
    } // end
} // end of FragmentHistory
