package com.odownard.symptomlogger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.odownard.symptomlogger.DataManager.DataManager;

import java.util.LinkedList;

/**
 * Created by olive_000 on 10/08/2015.
 */
public abstract class InfoFragment extends Fragment {
    public final static String ARG_ID = "id"; //Needs a symptom id passed to it
    public InfoFragment() {
    }

    protected long mId; //Id of the object

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mId = getArguments().getLong(ARG_ID);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.info_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_object, container, false);
        //((TextView) view.findViewById(R.id.symptom_text)).setText(Integer.toString(mId));

        LinkedList<Pair<Long, Integer>> raw = DataManager.getInstance().getLastNDaysEpisodes(getActivity().getContentResolver(), 7, mId);
        return view;
    } **/
}
