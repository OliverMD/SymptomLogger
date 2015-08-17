package com.odownard.symptomlogger;

import android.app.Activity;
import android.os.Bundle;
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
 * A simple {@link Fragment} subclass.
 * Use the {@link TagListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TagListFragment extends Fragment implements View.OnClickListener {

    private View.OnClickListener mListener;
    RecyclerView mRecyclerView;
    FloatingActionButton mFab;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment TagListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TagListFragment newInstance() {
        TagListFragment fragment = new TagListFragment();
        return fragment;
    }

    public TagListFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_tag_list, container, false);
        mFab = (FloatingActionButton) view.findViewById(R.id.fab_add);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.tagList);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTagDialogFragment dialogFragment = new AddTagDialogFragment();
                dialogFragment.show(getFragmentManager(), "Add Tag");
                mRecyclerView.getAdapter().notifyDataSetChanged();
        }
        });

        final String[] cols = {DataManagerContract.Tags.NAME};
        final int[] to = {R.id.name};

        //TODO: Change to a specific Tag Layout
        SingleCursorRecyclerAdapter singleCursorRecyclerAdapter = new SingleCursorRecyclerAdapter(R.layout.symptom_list_layout, DataManager.getInstance().getTags(getActivity().getContentResolver()),cols,to,this);
        singleCursorRecyclerAdapter.getCursor().setNotificationUri(getActivity().getContentResolver(), DataManagerContract.Tags.CONTENT_URI);

        mRecyclerView.setAdapter(singleCursorRecyclerAdapter);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = this;
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

    @Override
    public void onClick(View v) {

    }
}
