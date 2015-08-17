package com.odownard.symptomlogger.Adapters;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by olive_000 on 13/08/2015.
 */
public class SingleCursorRecyclerAdapter extends CursorRecyclerAdapter<SimpleViewHolder> {


    private int mSymptomLayout;
    private int[] mFrom;
    private int[] mTo;
    private String[] mOriginalFrom;
    private View.OnClickListener mClickListener;

    public SingleCursorRecyclerAdapter (int symptomLayout, Cursor c, String[] from, int[] to, View.OnClickListener clickListener) {
        super(c);
        mSymptomLayout = symptomLayout;
        mTo = to;
        mOriginalFrom = from;
        findColumns(c, from);
        mClickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }



    @Override
    public SimpleViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(mSymptomLayout, parent, false);
        v.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mClickListener.onClick(v);
            }
        });
        return new SimpleViewHolder(v, mTo);
    }

    @Override
    public void onBindViewHolder (SimpleViewHolder holder, Cursor cursor) {
        final int count = mTo.length;
        final int[] from = mFrom;

        for (int i = 0; i < count; i++) {
            holder.views[i].setText(cursor.getString(from[i]));
        }
    }

    /**
     * Create a map from an array of strings to an array of column-id integers in cursor c.
     * If c is null, the array will be discarded.
     *
     * @param c the cursor to find the columns from
     * @param from the Strings naming the columns of interest
     */
    private void findColumns(Cursor c, String[] from) {
        if (c != null) {
            int i;
            int count = from.length;
            if (mFrom == null || mFrom.length != count) {
                mFrom = new int[count];
            }
            for (i = 0; i < count; i++) {
                mFrom[i] = c.getColumnIndexOrThrow(from[i]);
            }
        } else {
            mFrom = null;
        }
    }

    @Override
    public Cursor swapCursor(Cursor c) {
        findColumns(c, mOriginalFrom);
        return super.swapCursor(c);
    }
}