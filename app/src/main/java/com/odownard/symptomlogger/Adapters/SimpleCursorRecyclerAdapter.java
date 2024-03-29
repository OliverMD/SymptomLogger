package com.odownard.symptomlogger.Adapters;/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 ARNAUD FRUGIER
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.odownard.symptomlogger.R;

public class SimpleCursorRecyclerAdapter extends CursorRecyclerAdapter<SimpleViewHolder> {

    public static final int SYMPTOM_TYPE = 0;
    public static final int TAG_TYPE = 1;

    private int mSymptomLayout;
    private int mTagLayout;
    private int[] mFrom;
    private int[] mTo;
    private String[] mOriginalFrom;
    private SymptomClickListener mSymptomClickListener;
    private TagClickListener mTagClickListener;

    public SimpleCursorRecyclerAdapter (int symptomLayout, int tagLayout, Cursor c, String[] from,
                                        int[] to, SymptomClickListener symptomClickListener,
                                        TagClickListener tagClickListener) {
        super(c);
        mSymptomLayout = symptomLayout;
        mTagLayout = tagLayout;
        mTo = to;
        mOriginalFrom = from;
        findColumns(c, from);
        mSymptomClickListener = symptomClickListener;
        mTagClickListener = tagClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        getCursor().moveToPosition(position);
        Log.v("Get View Type", getCursor().getString(getCursor().getColumnIndex("source")));
        if (getCursor().getString(getCursor().getColumnIndex("source")).equals( "symptoms")){
            return SYMPTOM_TYPE;
        } else {
            return TAG_TYPE;
        }
    }

    @Override
    public SimpleViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final View view;
        switch (viewType){
            case SYMPTOM_TYPE:
                //Assume symptom_list_layout.xml is used as the layout
                view = LayoutInflater.from(parent.getContext()).inflate(mSymptomLayout, parent, false);
                final FrameLayout sDeleteButton = (FrameLayout) view.findViewById(R.id.del_but);
                final FrameLayout sLogButton = (FrameLayout) view.findViewById(R.id.log_but);
                final FrameLayout sHistoryButton = (FrameLayout) view.findViewById(R.id.hist_but);
                sDeleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSymptomClickListener.onDeleteClick(view);
                    }
                });
                sLogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSymptomClickListener.onLogClick(view);
                    }
                });
                sHistoryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSymptomClickListener.onHistoryClick(view);
                    }
                });
                break;
            case TAG_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(mTagLayout, parent, false);
                final FrameLayout tDeleteButton = (FrameLayout) view.findViewById(R.id.del_but);
                final FrameLayout tLogButton = (FrameLayout) view.findViewById(R.id.log_but);
                tDeleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTagClickListener.onDeleteClick(view);
                    }
                });
                tLogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTagClickListener.onLogClick(view);
                    }
                });
                break;
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
        return new SimpleViewHolder(view, mTo);
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

class SimpleViewHolder extends RecyclerView.ViewHolder
{
    public TextView[] views;

    public SimpleViewHolder (View itemView, int[] to)
    {
        super(itemView);
        views = new TextView[to.length];
        for(int i = 0 ; i < to.length ; i++) {
            views[i] = (TextView) itemView.findViewById(to[i]);
        }
    }
}