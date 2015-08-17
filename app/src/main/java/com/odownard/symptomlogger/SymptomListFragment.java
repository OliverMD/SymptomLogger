package com.odownard.symptomlogger;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.odownard.symptomlogger.Adapters.SingleCursorRecyclerAdapter;
import com.odownard.symptomlogger.DataManager.DataManager;
import com.odownard.symptomlogger.DataManager.DataManagerContract;

/**
 * Created by olive_000 on 13/08/2015.
 */
public class SymptomListFragment extends Fragment implements android.view.View.OnClickListener {
    // TODO: Rename and change types of parameters
    private int mPosition;

    private OnSymptomListInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    RecyclerView mRecyclerView;
    FloatingActionButton mFab;

    // TODO: Rename and change types of parameters
    public static SymptomListFragment newInstance() {
        SymptomListFragment fragment = new SymptomListFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SymptomListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_symptoms, container, false);
        mFab = (FloatingActionButton) view.findViewById(R.id.fab_add);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.symptomList);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddSymptomDialogFragment dialogFragment = new AddSymptomDialogFragment();
                dialogFragment.show(getFragmentManager(), "Add Symptom");
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        final String[] cols = {DataManagerContract.Symptoms.NAME};
        final int[] to = {R.id.name};

        SingleCursorRecyclerAdapter singleCursorRecyclerAdapter = new SingleCursorRecyclerAdapter(R.layout.symptom_list_layout, DataManager.getInstance().getSymptoms(getActivity().getContentResolver()),cols,to,this);
        singleCursorRecyclerAdapter.getCursor().setNotificationUri(getActivity().getContentResolver(), DataManagerContract.Symptoms.CONTENT_URI);

        mRecyclerView.setAdapter(singleCursorRecyclerAdapter);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSymptomListInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSymptomListInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
    }

    @Override
    @UiThread
    public void onClick(View v) {
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
    public interface OnSymptomListInteractionListener {
        // TODO: Update argument type and name
        //public void onEpisodeLog(long symptomId, CharSequence name, int type);
    }

}
