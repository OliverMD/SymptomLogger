package com.odownard.symptomlogger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.odownard.symptomlogger.SymptomManager.SymptomManager;

import java.util.LinkedList;

/**
 * Created by olive_000 on 10/08/2015.
 */
public class HistoryViewObject extends Fragment {
    public final static String ARG_ID = "id"; //Needs a symptom id passed to it
    public HistoryViewObject() {
    }

    int mId;

    public static HistoryViewObject newInstance(int symptomId){
        HistoryViewObject historyViewObject = new HistoryViewObject();
        Bundle args = new Bundle();
        Log.v("History View", Integer.toString(symptomId));
        args.putInt(ARG_ID, symptomId);
        historyViewObject.setArguments(args);
        return historyViewObject;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mId = getArguments().getInt(ARG_ID);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_object, container, false);
        //((TextView) view.findViewById(R.id.symptom_text)).setText(Integer.toString(mId));

        LinkedList<Pair<Long, Integer>> raw = SymptomManager.getInstance().getLastNDaysEpisodes(getActivity().getContentResolver(), 7, mId);
        return view;
    }
}
