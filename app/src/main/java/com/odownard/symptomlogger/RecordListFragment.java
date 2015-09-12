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
import android.widget.TextView;
import android.widget.Toast;

import com.odownard.symptomlogger.Adapters.CursorRecyclerAdapter;
import com.odownard.symptomlogger.Adapters.SimpleCursorRecyclerAdapter;
import com.odownard.symptomlogger.Adapters.SymptomClickListener;
import com.odownard.symptomlogger.Adapters.TagClickListener;
import com.odownard.symptomlogger.DataManager.DataManager;
import com.odownard.symptomlogger.DataManager.DataManagerContract;

import java.util.Calendar;

public class RecordListFragment extends Fragment {

    RecyclerView mRecyclerView;
    FloatingActionButton mFab;
    RecordListListener mListener;

    public static RecordListFragment newInstance() {
        RecordListFragment fragment = new RecordListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public RecordListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        getActivity().setTitle("Symptom Logger");
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_list, container,false);
        mFab = (FloatingActionButton) view.findViewById(R.id.fab_add);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.symptomList);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        final String[] cols = {DataManagerContract.Symptoms.NAME};
        final int[] to = {R.id.name};

        SimpleCursorRecyclerAdapter simpleCursorRecyclerAdapter = new SimpleCursorRecyclerAdapter(
                R.layout.symptom_list_layout, R.layout.tag_list_layout,
                DataManager.getInstance().getHomeCursor(getActivity().getContentResolver()),
                cols, to, new SymptomClickListener() {
            @Override
            public void onDeleteClick(View symptomView) {
                DataManager.getInstance().deleteSymptom(getActivity().getContentResolver(),
                        mRecyclerView.getAdapter().getItemId(mRecyclerView
                                .getChildAdapterPosition(symptomView)));
                ((CursorRecyclerAdapter)mRecyclerView.getAdapter())
                        .changeCursor(DataManager.getInstance().
                                getHomeCursor(getActivity().getContentResolver()));
                mRecyclerView.getAdapter().notifyDataSetChanged();
                Toast.makeText(getActivity().getApplicationContext(), "Symptom Deleted!",
                        Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onLogClick(View symptomView) {
                Bundle data = new Bundle();
                data.putLong("ID", mRecyclerView.getAdapter()
                        .getItemId(mRecyclerView.getChildAdapterPosition(symptomView)));
                data.putLong("Datetime", Calendar.getInstance().getTimeInMillis());
                data.putCharSequence("Name", ((TextView) symptomView.findViewById(R.id.name))
                        .getText());
                NewEpisodeDialogFragment dialogFragment = new NewEpisodeDialogFragment();
                dialogFragment.setArguments(data);
                dialogFragment.show(getActivity().getSupportFragmentManager(), "New Episode");
                mListener.OnDataChanged();

            }

            @Override
            public void onHistoryClick(View symptomView) {
                long id = mRecyclerView.getAdapter()
                        .getItemId(mRecyclerView.getChildAdapterPosition(symptomView));
                getParentFragment().getChildFragmentManager().beginTransaction()
                        .replace(R.id.record_container, SymptomInfoFragment.newInstance(id))
                        .addToBackStack("Symptom Info")
                        .commit();

            }
        }, new TagClickListener() {
            @Override
            public void onDeleteClick(View TagView) {
                DataManager.getInstance().deleteTag(getActivity().getContentResolver(),
                        mRecyclerView.getAdapter()
                                .getItemId(mRecyclerView.getChildAdapterPosition(TagView)));
                ((CursorRecyclerAdapter)mRecyclerView.getAdapter())
                        .changeCursor(DataManager.getInstance().
                                getHomeCursor(getActivity().getContentResolver()));
                mRecyclerView.getAdapter().notifyDataSetChanged();
                Toast.makeText(getActivity().getApplicationContext(),
                        "Tag Deleted!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLogClick(View TagView) {
                DataManager.getInstance().addTagEpisode(getActivity().getContentResolver(),
                        Calendar.getInstance().getTimeInMillis(),
                        mRecyclerView.getAdapter().getItemId(mRecyclerView
                                .getChildAdapterPosition(TagView)));
                Toast.makeText(getActivity().getApplicationContext(), "Tag Episode Added!",
                        Toast.LENGTH_SHORT).show();
            }
        }
        );

        mRecyclerView.setAdapter(simpleCursorRecyclerAdapter);
        final Fragment temp = this;
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDialogFragment dialogFragment = new AddDialogFragment();
                dialogFragment.setTargetFragment(temp, 0);
                dialogFragment.show(getFragmentManager(), "Add Symptom or Tag");
            }
        });





        return view;
    }

    public void refreshAdapter(){
        ((CursorRecyclerAdapter)mRecyclerView.getAdapter())
                .changeCursor(DataManager.getInstance().
                        getHomeCursor(getActivity().getContentResolver()));
        mRecyclerView.getAdapter().notifyDataSetChanged();
        mListener.OnDataChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mListener = (RecordListListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnRecordListListener");
        }
    }

    public interface RecordListListener{
        void OnDataChanged();
    }
}
