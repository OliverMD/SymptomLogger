package com.odownard.symptomlogger;

import android.app.Activity;
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
import com.github.mikephil.charting.utils.PercentFormatter;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.odownard.symptomlogger.DataManager.DataManager;
import com.odownard.symptomlogger.DataManager.DataManagerContract;
import com.odownard.symptomlogger.InfoFragment;
import com.odownard.symptomlogger.R;

import java.text.DecimalFormat;
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

    private class SeverityValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public SeverityValueFormatter() {
            mFormat = new DecimalFormat("###,###,###");
        }

        @Override
        public String getFormattedValue(float value) {
            if (value == 110){
                return "";
            } else {
                return mFormat.format(value) + "%";
            }
        }
    }

    private class FrequencyValueFormatter implements ValueFormatter {
        private DecimalFormat mFormat;
        public FrequencyValueFormatter() {
            mFormat= new DecimalFormat("###,###,###");
        }

        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(value);
        }
    }

    private void configureSeverityGraph(int days, LineChart graph){
        ArrayList<Float> data = DataManager.getInstance()
                .getLastNDaysEpisodesDayNormalised(getActivity().getContentResolver(), days, mId);
        Log.v("Days",(DataViewerHelper.getInstance()
                .lastNDayNames(this.getActivity().getApplicationContext(),days).toString()));
        ArrayList<String> xVals = (DataViewerHelper.getInstance()
                .lastNDayNames(this.getActivity().getApplicationContext(),days));
        LineDataSet lineDataSet = new LineDataSet(DataViewerHelper.getInstance()
                .floatToEntry(data), "");
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawFilled(false);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setColor(getResources().getColor(R.color.ColorAccentDark));
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setDrawCubic(true);
        lineDataSet.setLineWidth(2);
        LineData lineData = new LineData(xVals, lineDataSet);
        graph.getAxisRight().setEnabled(false);
        graph.getLegend().setEnabled(false);
        graph.setDescription("");
        graph.getAxisLeft().setSpaceTop(15f);
        graph.getXAxis().setDrawGridLines(false);
        graph.getAxisLeft().setDrawGridLines(false);
        graph.getAxisLeft().setAxisMaxValue(110f);
        graph.getAxisLeft().setAxisMinValue(0f);
        graph.getAxisLeft().setLabelCount(5, false);
        graph.getAxisLeft().setValueFormatter(new SeverityValueFormatter());
        graph.setData(lineData);
        graph.invalidate();
    }
    private void configureFrequencyGraph(int days, BarChart chart){
        ArrayList<Float> data = DataManager.getInstance().getLastNDaysEpisodesFrequency(
                getActivity().getContentResolver(), days, mId
        );
        Log.v("Freq Data", data.toString());
        float maxVal = 0;
        for (Float val: data){
            if(val > maxVal){
                maxVal = val;
            }
        }

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
        chart.getAxisLeft().setLabelCount(Math.round(maxVal), false);
        chart.getAxisLeft().setValueFormatter(new FrequencyValueFormatter());
        chart.getLegend().setEnabled(false);
        chart.setDescription("");
        chart.setData(barData);
        chart.invalidate();

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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

}