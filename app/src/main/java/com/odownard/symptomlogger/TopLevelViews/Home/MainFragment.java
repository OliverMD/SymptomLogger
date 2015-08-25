package com.odownard.symptomlogger.TopLevelViews.Home;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.odownard.symptomlogger.Adapters.SimpleCursorRecyclerAdapter;
import com.odownard.symptomlogger.DataManager.DataManager;
import com.odownard.symptomlogger.DataManager.DataManagerContract;
import com.odownard.symptomlogger.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class MainFragment extends Fragment implements android.view.View.OnClickListener {
    private OnFragmentInteractionListener mListener;
    RecyclerView mRecyclerView;

    // TODO: Rename and change types of parameters
    public static MainFragment newInstance(int position) {
        MainFragment fragment = new MainFragment();
        return fragment;
    }
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quicksymptoms, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.cardList);
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager glm = new GridLayoutManager(getActivity(), 2);
        glm.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(glm);
        final String[] cols = {DataManagerContract.Symptoms.NAME};
        final int[] to = {R.id.name};
        SimpleCursorRecyclerAdapter simpleCursorRecyclerAdapter = new SimpleCursorRecyclerAdapter(R.layout.symptom_layout,R.layout.tag_layout, DataManager.getInstance().getHomeCursor(getActivity().getContentResolver()),cols,to,this);
        mRecyclerView.setAdapter(simpleCursorRecyclerAdapter);
        getActivity().setTitle("Home");
        return view;
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
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    @Override
    public void onClick(View v) {
        long id = mRecyclerView.getAdapter().getItemId(mRecyclerView.getChildAdapterPosition(v));
        int type = mRecyclerView.getAdapter().getItemViewType(mRecyclerView.getChildAdapterPosition(v));
        CharSequence name = "";
        if (type == SimpleCursorRecyclerAdapter.TAG_TYPE){
            name = DataManager.getInstance().getTag(getActivity().getContentResolver(),id).getAsString(DataManagerContract.Tags.NAME);
        } else if (type == SimpleCursorRecyclerAdapter.SYMPTOM_TYPE){
            name = DataManager.getInstance().getSymptom(getActivity().getContentResolver(), id).getAsString(DataManagerContract.Symptoms.NAME);
        }
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onEpisodeLog(id, name, type);
        }
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
        void onEpisodeLog(long symptomId, CharSequence name, int type);
    }

}
