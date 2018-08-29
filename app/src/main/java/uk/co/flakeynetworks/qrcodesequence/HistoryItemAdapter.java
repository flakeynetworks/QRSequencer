package uk.co.flakeynetworks.qrcodesequence;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Richard Stokes on 12/30/2017.
 */

public class HistoryItemAdapter extends ArrayAdapter<ScanResult> {


    private List<ScanResult> scanResults;
    private LayoutInflater mInflater;


    public HistoryItemAdapter(@NonNull Context context, int resource, @NonNull List<ScanResult> objects) {

        super(context, resource, objects);

        scanResults = objects;
        mInflater = LayoutInflater.from(context);
    } // end of constructor


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null)
            convertView = mInflater.inflate(R.layout.list_history_item, parent, false);

        TextView message = convertView.findViewById(R.id.message);

        ScanResult result = scanResults.get(position);
        message.setText(result.getMessage());

        TextView date = convertView.findViewById(R.id.dateTaken);
        date.setText(result.getDateAndTimeTaken());

        return convertView;
    } // end of getView
} // end of HistoryItemAdapter
