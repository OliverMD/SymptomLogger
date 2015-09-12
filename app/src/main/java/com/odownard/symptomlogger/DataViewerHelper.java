package com.odownard.symptomlogger;

import android.content.Context;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

/**
 * Created by olive_000 on 07/09/2015.
 */
public class DataViewerHelper {
    private static final DataViewerHelper instance = new DataViewerHelper();
    private DataViewerHelper(){}
    public static DataViewerHelper getInstance(){
        return instance;
    }
    public ArrayList<String> lastNDayNames(Context context, int days){
        ArrayList<String> retList = new ArrayList<>(days);
        int cnt = days;
        GregorianCalendar calendar = new GregorianCalendar();
        while (cnt > 0){
            cnt -=1;
            retList.add(calendar.getDisplayName(GregorianCalendar.DAY_OF_WEEK, Calendar.SHORT
                    ,context.getResources().getConfiguration().locale));
            calendar.add(GregorianCalendar.DAY_OF_YEAR, -1);
        }
        Collections.reverse(retList);
        return  retList;
    }

    public ArrayList<Entry> floatToEntry(ArrayList<Float> data){
        ArrayList<Entry> retList = new ArrayList<>(data.size());
        for (int idx = 0; idx < data.size(); idx++) {
            retList.add(new Entry(data.get(idx), idx));
        }
        return retList;
    }

    public ArrayList<BarEntry> floatToBarEntry(ArrayList<Float> data){
        ArrayList<BarEntry> retList = new ArrayList<>(data.size());
        for (int idx = 0; idx < data.size(); idx++){
            retList.add(new BarEntry(data.get(idx), idx));
        }
        return  retList;
    }
}
