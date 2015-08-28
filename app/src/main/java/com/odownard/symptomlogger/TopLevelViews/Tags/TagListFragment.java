package com.odownard.symptomlogger.TopLevelViews.Tags;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.odownard.symptomlogger.Adapters.CursorRecyclerAdapter;
import com.odownard.symptomlogger.Adapters.ItemClickListener;
import com.odownard.symptomlogger.Adapters.SingleCursorRecyclerAdapter;
import com.odownard.symptomlogger.DataManager.DataManager;
import com.odownard.symptomlogger.DataManager.DataManagerContract;
import com.odownard.symptomlogger.DividerItemDecoration;
import com.odownard.symptomlogger.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TagListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TagListFragment extends Fragment implements ItemClickListener {

    private OnTagListInteractionListener mListener;
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
        final Fragment temp = this;
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTagDialogFragment dialogFragment = new AddTagDialogFragment();
                dialogFragment.setTargetFragment(temp, 0);
                dialogFragment.show(getFragmentManager(), "Add Tag");
        }
        });

        final String[] cols = {DataManagerContract.Tags.NAME};
        final int[] to = {R.id.name};

        //TODO: Change to a specific Tag Layout
        SingleCursorRecyclerAdapter singleCursorRecyclerAdapter =
                new SingleCursorRecyclerAdapter(R.layout.symptom_list_layout,
                        DataManager.getInstance()
                                .getTags(getActivity().getContentResolver()),cols,to,this);
        singleCursorRecyclerAdapter.getCursor()
                .setNotificationUri(getActivity().getContentResolver(),
                        DataManagerContract.Tags.CONTENT_URI);

        mRecyclerView.setAdapter(singleCursorRecyclerAdapter);
        getActivity().setTitle("Tags");
        Log.v("TAG", "TAG VIEW CREATED");

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnTagListInteractionListener) activity;
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

    public void refreshAdapter(){
        ((CursorRecyclerAdapter)mRecyclerView.getAdapter())
                .changeCursor(DataManager.getInstance().getTags(getActivity().getContentResolver()));
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        mListener.onTagListResume();
        super.onResume();
    }

    @Override
    public void onClick(View parent, int clickedView) {
        if (clickedView == R.id.del_but){
            long id = mRecyclerView.getAdapter()
                    .getItemId(mRecyclerView.getChildAdapterPosition(parent));
            Log.v("Click", "Delete Pressed " + Long.toString(id));
            DataManager.getInstance().deleteTag(getActivity().getContentResolver(), id);
            Toast.makeText(getActivity().getApplicationContext(), "Tag Deleted!", Toast.LENGTH_SHORT)
                    .show();
            ((CursorRecyclerAdapter)mRecyclerView.getAdapter())
                    .changeCursor(DataManager.getInstance().
                            getTags(getActivity().getContentResolver()));
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    public interface OnTagListInteractionListener {
        void onTagListResume();
    }
}
