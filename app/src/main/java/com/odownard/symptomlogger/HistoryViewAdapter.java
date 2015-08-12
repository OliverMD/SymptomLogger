package com.odownard.symptomlogger;

import android.content.ContentResolver;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Pair;

import com.odownard.symptomlogger.SymptomManager.SymptomManager;

import java.util.ArrayList;

public class HistoryViewAdapter extends FragmentStatePagerAdapter {
    ArrayList<Pair<Integer, String>> mSymptoms;

    public HistoryViewAdapter(FragmentManager fm, ContentResolver resolver) {
        super(fm);
        mSymptoms = SymptomManager.getInstance().getSymptomsList(resolver);
    }

    @Override
    public Fragment getItem(int position) {
        return HistoryViewObject.newInstance(mSymptoms.get(position).first);
    }

    @Override
    public int getCount() {
        return mSymptoms.size();
    }

    @Override
    public CharSequence getPageTitle(int position){
        return mSymptoms.get(position).second;
    }
}
