package com.odownard.symptomlogger;


import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.odownard.symptomlogger.DataManager.DataManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackFragment extends Fragment implements OnItemSelectedListener {
    public static TrackFragment newInstance() {
        TrackFragment fragment = new TrackFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    int mDays;
    Spinner mDateSpinner;
    BarChart mFreqChart;

    public TrackFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mDays = 7;
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.track_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track,container, false);
        mFreqChart = (BarChart) view.findViewById(R.id.overall_freq_graph);
        mFreqChart.setLogEnabled(true);
        configureOverallFreqChart(mDays, mFreqChart);

        mDateSpinner = (Spinner) view.findViewById(R.id.date_spinner);
        mDateSpinner.setOnItemSelectedListener(this);
        return view;
    }
    private void configureOverallFreqChart(int days, BarChart chart){
        ArrayList<Float> data = DataManager.getInstance().getLastNDaysAllEpisodesFrequency(
                getActivity().getContentResolver(),
                days
        );
        ArrayList<String> xVals = DataViewerHelper.getInstance()
                .lastNDayNames(this.getActivity().getApplicationContext(), days);
        BarDataSet barDataSet = new BarDataSet(DataViewerHelper.getInstance()
        .floatToBarEntry(data), "");
        barDataSet.setDrawValues(false);
        barDataSet.setColor(getResources().getColor(R.color.ColorAccentDark));
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        BarData barData = new BarData(DataViewerHelper.getInstance()
                .lastNDayNames(this.getActivity().getApplicationContext(), days), barDataSet);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setDescription("");
        chart.setData(barData);
        chart.invalidate();
    }

    @UiThread
    private void updateFreqChart(){
        ArrayList<Float> data = DataManager.getInstance().getLastNDaysAllEpisodesFrequency(
                getActivity().getContentResolver(),
                mDays
        );
        ArrayList<String> xVals = DataViewerHelper.getInstance()
                .lastNDayNames(this.getActivity().getApplicationContext(), mDays);
        BarDataSet barDataSet = new BarDataSet(DataViewerHelper.getInstance()
                .floatToBarEntry(data), "");
        barDataSet.setDrawValues(false);
        barDataSet.setColor(getResources().getColor(R.color.ColorAccentDark));
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        mFreqChart.getBarData().removeDataSet(0);
        mFreqChart.getBarData().addDataSet(barDataSet);
        mFreqChart.invalidate();
        mFreqChart.notifyDataSetChanged();
        mFreqChart.invalidate();

    }

    @Override
    public void onResume() {
        Log.v("TRACK", "RESUMED");
        super.onResume();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                mDays = 7;
                break;
            case 1:
                mDays = 14;
                break;
            case 2:
                mDays = 28;
                break;
            case 3:
                mDays = 90;
                break;
            default:
                mDays = 7;
                parent.setSelection(0);
        }
        if (mFreqChart != null) configureOverallFreqChart(mDays,mFreqChart);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.update:
                configureOverallFreqChart(mDays, mFreqChart);
        }
        return true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void OnDataChange(){
        configureOverallFreqChart(mDays, mFreqChart);
    }
}
