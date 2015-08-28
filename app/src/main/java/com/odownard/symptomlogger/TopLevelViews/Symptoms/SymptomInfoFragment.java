package com.odownard.symptomlogger.TopLevelViews.Symptoms;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.odownard.symptomlogger.DataManager.DataManager;
import com.odownard.symptomlogger.DataManager.DataManagerContract;
import com.odownard.symptomlogger.InfoFragment;
import com.odownard.symptomlogger.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.LinkedList;

/**
 * Created by olive_000 on 18/08/2015.
 */
public class SymptomInfoFragment extends InfoFragment {

    String mName;

    public static InfoFragment newInstance(long symptomId) {
        SymptomInfoFragment symptomInfoFragment = new SymptomInfoFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, symptomId);
        symptomInfoFragment.setArguments(args);
        return symptomInfoFragment;
    }

    public SymptomInfoFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.symptom_info_fragment, container, false);

        ContentValues symptomValues = DataManager
                .getInstance().getSymptom(getActivity().getContentResolver(), mId);
        mName = symptomValues.getAsString(DataManagerContract.Symptoms.NAME);
        getActivity().setTitle(mName);

        LineChart graph = (LineChart) view.findViewById(R.id.line_graph);
        configureSeverityGraph(7, graph);

        BarChart barChart = (BarChart) view.findViewById(R.id.freq_line_graph);
        configureFrequencyGraph(7, barChart);
        //DataManager.getInstance().getLastNDaysEpisodes(getActivity().getContentResolver(), 7, mId);
        //Log.v("Days ", DataManager.getInstance().getLastNDaysEpisodesDayNormalised(getActivity().getContentResolver(), 7, mId).toString());

        return view;
    }

    private void configureSeverityGraph(int days, LineChart graph){
        ArrayList<Float> data = DataManager.getInstance()
                .getLastNDaysEpisodesDayNormalised(getActivity().getContentResolver(), days, mId);
        Log.v("Days",createXValues(days).toString());
        ArrayList<String> xVals = createXValues(days);
        LineDataSet lineDataSet = new LineDataSet(dataToEntries(data), "");
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawFilled(false);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setColor(getResources().getColor(R.color.ColorAccentDark));
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setDrawCubic(true);
        lineDataSet.setLineWidth(2);
        LineData lineData = new LineData(xVals, lineDataSet);
        graph.setData(lineData);
        graph.getAxisRight().setEnabled(false);
        graph.getLegend().setEnabled(false);
        graph.setDescription("");
        graph.getXAxis().setDrawGridLines(false);
        graph.getAxisLeft().setDrawGridLines(false);
        graph.invalidate();
    }
    private void configureFrequencyGraph(int days, BarChart chart){
        ArrayList<Float> data = DataManager.getInstance().getLastNDaysEpisodesFrequency(
                getActivity().getContentResolver(), days, mId
        );
        Log.v("Freq Data", data.toString());

        BarDataSet barDataSet = new BarDataSet(dataToBarEntries(data), "");
        barDataSet.setDrawValues(false);
        barDataSet.setColor(getResources().getColor(R.color.ColorAccentDark));
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        BarData barData = new BarData(createXValues(days), barDataSet);
        chart.setData(barData);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setDescription("");
        chart.invalidate();

    }

    private ArrayList<String> createXValues(int days){
        ArrayList<String> retList = new ArrayList<>(days);
        int cnt = days;
        GregorianCalendar calendar = new GregorianCalendar();
        while (cnt > 0){
            cnt -=1;
            retList.add(calendar.getDisplayName(GregorianCalendar.DAY_OF_WEEK, Calendar.SHORT
                    ,getResources().getConfiguration().locale));
            calendar.add(GregorianCalendar.DAY_OF_YEAR, -1);
        }
        Collections.reverse(retList);
        return  retList;
    }

    private ArrayList<String> generateReverseNumbers(int lim){
        ArrayList<String> retVals = new ArrayList<>(lim);
        for (int i = lim; i>0; i--){
            retVals.add(Integer.toString(i));
        }
        return retVals;
    }

    private ArrayList<Entry> dataToEntries(ArrayList<Float> data) {
        ArrayList<Entry> retList = new ArrayList<>(data.size());
        for (int idx = 0; idx < data.size(); idx++) {
            retList.add(new Entry(data.get(idx), data.size() - idx - 1));
        }
        return retList;
    }

    private ArrayList<BarEntry> dataToBarEntries(ArrayList<Float> data){
        ArrayList<BarEntry> retList = new ArrayList<>(data.size());
        for (int idx = 0; idx < data.size(); idx++){
            retList.add(new BarEntry(data.get(idx), idx));
        }
        return  retList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_item:
                DataManager.getInstance().deleteSymptom(getActivity().getContentResolver(), mId);
                getActivity().getSupportFragmentManager().popBackStack();
                Toast.makeText(getActivity().getApplicationContext()
                        , "Symptom Deleted!", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
