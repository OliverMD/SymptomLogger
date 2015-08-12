package com.odownard.symptomlogger;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.odownard.symptomlogger.SymptomManager.SymptomManager;

import java.util.LinkedList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private ViewPager mViewPager;
    private HistoryViewAdapter mHistoryViewAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_history, container, false);
        /**
        GraphView graph = (GraphView) v.findViewById(R.id.graph);

        //LinkedList<DataPoint> raw = SymptomManager.getInstance().getNEpisodes(getActivity().getContentResolver(), 10);

        LinkedList<DataPoint> raw = SymptomManager.getInstance().getLastNDaysEpisodes(getActivity().getContentResolver(), 7);
        DataPoint data[] = new DataPoint[raw.size()];
        Log.v(getTag(),raw.toString());
        data = raw.toArray(data);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(data);
        graph.addSeries(series);
         **/
        //graph.getGridLabelRenderer().setLabelFormatter(new );
        mHistoryViewAdapter = new HistoryViewAdapter(getActivity().getSupportFragmentManager(), getActivity().getContentResolver());
        mViewPager = (ViewPager) v.findViewById(R.id.history_pager);
        mViewPager.setAdapter(mHistoryViewAdapter);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}

