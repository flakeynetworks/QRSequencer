package uk.co.flakeynetworks.qrcodesequence;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import uk.co.flakeynetworks.qrcodesequence.model.ScanResult;

/**
 * Created by Richard Stokes on 12/30/2017.
 */

public class HistoryItemAdapter extends RecyclerView.Adapter<HistoryItemAdapter.ScanHolder> {


    private List<ScanResult> scanResults;
    private MainActivity mainActivity;


    public class ScanHolder extends RecyclerView.ViewHolder {


        private TextView message, date;
        private ScanResult result;
        private View view;


        public ScanHolder(View view) {

            super(view);

            this.view = view;

            message = view.findViewById(R.id.message);
            date = view.findViewById(R.id.dateTaken);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.loadScanResults(result);
                } // end of onClick
            });
        } // end of constructor


        public ScanResult getResult() { return result; } // end of getResult


        public void updateContents(ScanResult result) {

            this.result = result;

            message.setText(result.getMessage());
            date.setText(result.getDateAndTimeTaken());
        } // end of updateContents


        public LinearLayout getForeground() { return view.findViewById(R.id.foreground); } // end of getForeground
        public LinearLayout getBackground() { return view.findViewById(R.id.background); } // end of getForeground
    } // end of ScanHolder


    public HistoryItemAdapter(@NonNull MainActivity activity, @NonNull List<ScanResult> objects) {

        this.mainActivity = activity;
        scanResults = objects;
    } // end of constructor


    @Override
    public ScanHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_history_item, parent, false);

        return new ScanHolder(itemView);
    } // end of onCreateViewHolder


    @Override
    public void onBindViewHolder(ScanHolder holder, int position) {

        ScanResult result = scanResults.get(position);
        holder.updateContents(result);
    } // end of onBindViewHolder


    @Override
    public int getItemCount() { return scanResults.size(); } // end of getItemCount
} // end of HistoryItemAdapter
