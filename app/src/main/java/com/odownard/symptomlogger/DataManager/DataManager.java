package com.odownard.symptomlogger.DataManager;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;


import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

/**
 * Created by olive_000 on 03/08/2015.
 * Provides useful functions for interacting with the database
 */
public class DataManager {

    private DataManager() {
    }

    private static final DataManager instance = new DataManager();

    public static DataManager getInstance(){
        return instance;
    }

    public Cursor getSymptoms(ContentResolver resolver){
        final String[] cols = {DataManagerContract.Symptoms.SYMPTOM_ID, DataManagerContract.Symptoms.NAME, DataManagerContract.Symptoms.DESCRIPTION};
        return resolver.query(DataManagerContract.Symptoms.CONTENT_URI, cols,null,null,null );
    }
    public Cursor getTags(ContentResolver resolver){
        final String[] cols = {DataManagerContract.Tags.TAG_ID, DataManagerContract.Tags.NAME, DataManagerContract.Tags.DESCRIPTION};
        return resolver.query(DataManagerContract.Tags.CONTENT_URI, cols, null, null, null);
    }
    public ContentValues getSymptom(ContentResolver resolver, long id){
        final String[] cols = {DataManagerContract.Symptoms.SYMPTOM_ID, DataManagerContract.Symptoms.NAME, DataManagerContract.Symptoms.DESCRIPTION};
        Cursor cursor = resolver.query(Uri.withAppendedPath(DataManagerContract.Symptoms.CONTENT_URI,"/"+Long.toString(id)),cols,null,null,null);
        cursor.moveToFirst();
        ContentValues values = new ContentValues(3);
        values.put(DataManagerContract.Symptoms.SYMPTOM_ID, cursor.getString(0));
        values.put(DataManagerContract.Symptoms.NAME, cursor.getString(cursor.getColumnIndex(DataManagerContract.Symptoms.NAME)));
        values.put(DataManagerContract.Symptoms.DESCRIPTION, cursor.getString(cursor.getColumnIndex(DataManagerContract.Symptoms.DESCRIPTION)));
        return values;
    }
    public ContentValues getTag(ContentResolver resolver, long id){
        final String[] cols = {DataManagerContract.Tags.TAG_ID, DataManagerContract.Tags.NAME, DataManagerContract.Tags.DESCRIPTION};
        Cursor cursor = resolver.query(Uri.withAppendedPath(DataManagerContract.Tags.CONTENT_URI,"/"+Long.toString(id)),cols,null,null,null);
        cursor.moveToFirst();
        ContentValues values = new ContentValues(3);
        values.put(DataManagerContract.Tags.TAG_ID, cursor.getString(cursor.getColumnIndex(DataManagerContract.Tags.TAG_ID)));
        values.put(DataManagerContract.Tags.NAME, cursor.getString(cursor.getColumnIndex(DataManagerContract.Tags.NAME)));
        values.put(DataManagerContract.Tags.DESCRIPTION, cursor.getString(cursor.getColumnIndex(DataManagerContract.Tags.DESCRIPTION)));
        return values;
    }

    public Cursor getHomeCursor(ContentResolver resolver){
        return resolver.query(Uri.parse("content://" + DataManagerProvider.AUTHORITY + "/home-elements"),null,null,null,null);
    }

    public ArrayList<Pair<Integer, String>> getSymptomsList(ContentResolver resolver){
        Cursor cursor = getSymptoms(resolver);

        ArrayList<Pair<Integer, String>> ret = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()){
            ret.add(new Pair<Integer, String>(cursor.getInt(cursor.getColumnIndex(DataManagerContract.Symptoms.SYMPTOM_ID)),
                    cursor.getString(cursor.getColumnIndex(DataManagerContract.Symptoms.NAME))));
        }
        cursor.close();

        return ret;
    }

    public Boolean addSymptom(ContentResolver resolver, String name, String desc){
        ContentValues values = new ContentValues();
        values.put(DataManagerContract.Symptoms.NAME, name);
        values.put(DataManagerContract.Symptoms.DESCRIPTION, desc);

        resolver.insert(DataManagerContract.Symptoms.CONTENT_URI, values);
        return true;
    }

    public Boolean deleteSymptom(ContentResolver resolver, long id){
        resolver.delete(Uri.withAppendedPath(DataManagerContract.Symptoms.CONTENT_URI, Long.toString(id)), null, null);
        return true;
    }
    public Boolean deleteTag(ContentResolver resolver, long id){
        resolver.delete(Uri.withAppendedPath(DataManagerContract.Tags.CONTENT_URI, Long.toString(id)), null, null);
        return true;
    }

    public Boolean addTag(ContentResolver resolver, String name, String desc){
        ContentValues values = new ContentValues();
        values.put(DataManagerContract.Tags.NAME, name);
        values.put(DataManagerContract.Tags.DESCRIPTION, desc);
        resolver.insert(DataManagerContract.Tags.CONTENT_URI, values);
        return true;
    }

    public Boolean addEpisode(ContentResolver resolver, long datetime, long symptomId, float discomfort){
        ContentValues values = new ContentValues();
        values.put(DataManagerContract.Episodes.DATETIME, datetime);
        values.put(DataManagerContract.Episodes.SYMPTOM_ID, symptomId);
        values.put(DataManagerContract.Episodes.DISCOMFORT, discomfort);

        resolver.insert(DataManagerContract.Episodes.CONTENT_URI, values);

        return true;
    }

    public Boolean addTagEpisode(ContentResolver resolver, long datetime, long tagId){
        ContentValues values = new ContentValues();
        values.put(DataManagerContract.TagEpisodes.DATETIME, datetime);
        values.put(DataManagerContract.TagEpisodes.TAG_ID, tagId);

        resolver.insert(DataManagerContract.TagEpisodes.CONTENT_URI, values);
        return true;
    }

    public LinkedList<Pair<Long, Integer>> getLastNDaysEpisodes(ContentResolver resolver, int n, long id){
        long lastDate =  Calendar.getInstance().getTimeInMillis() - (86400000L * n);
        final String[] cols = {DataManagerContract.Episodes.DATETIME, DataManagerContract.Episodes.DISCOMFORT};
        Cursor cursor = resolver.query(Uri.withAppendedPath(DataManagerContract.Episodes.CONTENT_URI, "/"
                + DataManagerContract.Episodes.DATETIME + "/limit/" + Long.toString(lastDate)),
                cols, DataManagerContract.Episodes.SYMPTOM_ID + " = " + Long.toString(id), null, null);
        LinkedList<Pair<Long, Integer>> retVal = new LinkedList<>();
        while (cursor.moveToNext()){
            retVal.add(new Pair<Long, Integer>(Double.doubleToLongBits
                    ((cursor.getDouble(cursor.getColumnIndex(DataManagerContract.Episodes.DATETIME))
                            - (Calendar.getInstance().getTimeInMillis() - (86400000L * n)))/86400000)
                    , cursor.getInt(cursor.getColumnIndex(DataManagerContract.Episodes.DISCOMFORT))));
        }
        cursor.close();
        Log.v("Data", retVal.toString());
        return retVal;
    }

    public ArrayList<Float> getLastNDaysEpisodesDayNormalised(ContentResolver resolver, int n, long id){
        long lastDate = Calendar.getInstance().getTimeInMillis() - (86400000L * n);
        addEpisode(resolver,Calendar.getInstance().getTimeInMillis() - (86400000L * 5), id, 34.0f );
        final String[] cols = {DataManagerContract.Episodes.DATETIME, DataManagerContract.Episodes.DISCOMFORT};
        Cursor cursor = resolver.query(Uri.withAppendedPath(DataManagerContract.Episodes.CONTENT_URI, "/"
                        + DataManagerContract.Episodes.DATETIME + "/limit/" + Long.toString(lastDate)),
                cols, DataManagerContract.Episodes.SYMPTOM_ID + " = " + Long.toString(id), null, null);
        ArrayList<Float> retList = new ArrayList<Float>(n);
        while(retList.size()<n) retList.add(0.0f);
        int day = n-1;
        Log.v("Mid Point", retList.toString());
        while (cursor.moveToNext() && day >= 0){

            if (cursor.getLong(cursor.getColumnIndex(DataManagerContract.Episodes.DATETIME))
                    > (Calendar.getInstance().getTimeInMillis() - (86400000L * day))){
                day -=1;
                cursor.moveToPrevious();
            } else{
                retList.set(day, retList.get(day) + cursor.getFloat(cursor.getColumnIndex(DataManagerContract.Episodes.DISCOMFORT)));
            }
        }
        Log.v("Mid Point", retList.toString());
        float maxVal = 1.0f;
        for (int idx = 0; idx < retList.size(); idx ++){
            if (retList.get(idx) > maxVal) maxVal = retList.get(idx);
        }
        for (int idx = 0; idx < retList.size(); idx++){
            retList.set(idx, retList.get(idx)/maxVal);
        }
        Log.v("Mid Point", retList.toString());
        cursor.close();
        return retList;
    }
}
