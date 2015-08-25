package com.odownard.symptomlogger.TopLevelViews.Tags;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.odownard.symptomlogger.DataManager.DataManager;
import com.odownard.symptomlogger.DataManager.DataManagerContract;
import com.odownard.symptomlogger.InfoFragment;
import com.odownard.symptomlogger.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by olive_000 on 21/08/2015.
 */
public class TagInfoFragment extends InfoFragment {

    String mName;

    public static InfoFragment newInstance(long symptomId) {
        TagInfoFragment tagInfoFragment = new TagInfoFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, symptomId);
        tagInfoFragment.setArguments(args);
        return tagInfoFragment;
    }

    public TagInfoFragment() {
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

        ContentValues symptomValues = DataManager.getInstance().getSymptom(getActivity().getContentResolver(), mId);
        mName = symptomValues.getAsString(DataManagerContract.Symptoms.NAME);
        getActivity().setTitle(mName);

        LineChart graph = (LineChart) view.findViewById(R.id.line_graph);
        ArrayList<Float> data = DataManager.getInstance().getLastNDaysEpisodesDayNormalised(getActivity().getContentResolver(), 7, mId);
        ArrayList<String> xVals = new ArrayList<>(Arrays.asList("7", "6", "5", "4", "3", "2", "1"));
        LineDataSet lineDataSet = new LineDataSet(dataToEntries(data), "Test Label");
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setDrawCubic(true);
        LineData lineData = new LineData(xVals, lineDataSet);
        graph.setData(lineData);
        graph.getAxisLeft().setEnabled(false);
        graph.getLegend().setEnabled(false);
        graph.setDescription("");
        graph.invalidate();
        //DataManager.getInstance().getLastNDaysEpisodes(getActivity().getContentResolver(), 7, mId);
        //Log.v("Days ", DataManager.getInstance().getLastNDaysEpisodesDayNormalised(getActivity().getContentResolver(), 7, mId).toString());

        return view;
    }

    private ArrayList<Entry> dataToEntries(ArrayList<Float> data) {
        ArrayList<Entry> retList = new ArrayList<>(data.size());
        for (int idx = 0; idx < data.size(); idx++) {
            retList.add(new Entry(data.get(idx), data.size() - idx - 1));
        }
        return retList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_item:
                DataManager.getInstance().deleteSymptom(getActivity().getContentResolver(), mId);
                getActivity().getSupportFragmentManager().popBackStack();
                Toast.makeText(getActivity().getApplicationContext(), "Symptom Deleted!", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
