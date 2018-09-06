package uk.co.flakeynetworks.qrcodesequence.ui;

import android.app.Fragment;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import uk.co.flakeynetworks.qrcodesequence.HistoryItemAdapter;
import uk.co.flakeynetworks.qrcodesequence.MainActivity;
import uk.co.flakeynetworks.qrcodesequence.R;
import uk.co.flakeynetworks.qrcodesequence.model.ScanHistory;
import uk.co.flakeynetworks.qrcodesequence.model.ScanResult;

/**
 * Created by Richard Stokes on 8/29/2018.
 */

public class FragmentHistory extends Fragment {


    private ScanHistory history = new ScanHistory();
    private RefreshHistoryTask refreshHistoryTask;
    private RecyclerView.Adapter historyListAdapter;


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

        RecyclerView historyList = getView().findViewById(R.id.historyList);
        historyListAdapter = new HistoryItemAdapter((MainActivity) getActivity(), results);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1);
        historyList.setLayoutManager(mLayoutManager);
        historyList.setItemAnimator(new DefaultItemAnimator());
        historyList.setAdapter(historyListAdapter);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            } // end of onMove

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                if(swipeDir == ItemTouchHelper.LEFT) {

                    // Get the scan result
                    ScanResult result = ((HistoryItemAdapter.ScanHolder) viewHolder).getResult();

                    // Delete the item
                    if(history.delete(result))
                        // Remove item from backing list here
                        historyListAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                } // end of if
            } // end of onSwiped

            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                        RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                        int actionState, boolean isCurrentlyActive) {
                final View foregroundView = ((HistoryItemAdapter.ScanHolder) viewHolder).getForeground();
                getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                        actionState, isCurrentlyActive);
            } // end of onChildDrawOver

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                final View foregroundView = ((HistoryItemAdapter.ScanHolder) viewHolder).getForeground();
                getDefaultUIUtil().clearView(foregroundView);
            } // end of clearView


            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                    RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                final View foregroundView = ((HistoryItemAdapter.ScanHolder) viewHolder).getForeground();

                getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                        actionState, isCurrentlyActive);
            } // end of ChildDraw
        });

        itemTouchHelper.attachToRecyclerView(historyList);
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
