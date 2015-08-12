package com.odownard.symptomlogger.SymptomManager;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Pair;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by olive_000 on 03/08/2015.
 * Provides useful functions for interacting with the database
 */
public class SymptomManager {

    private SymptomManager() {
    }

    private static final SymptomManager instance = new SymptomManager();

    public static SymptomManager getInstance(){
        return instance;
    }

    public Cursor getSymptoms(ContentResolver resolver){
        final String[] cols = {SymptomManagerContract.Symptoms.SYMPTOM_ID,SymptomManagerContract.Symptoms.NAME};
        return resolver.query(SymptomManagerContract.Symptoms.CONTENT_URI, cols,null,null,null );
    }

    public ArrayList<Pair<Integer, String>> getSymptomsList(ContentResolver resolver){
        Cursor cursor = getSymptoms(resolver);

        ArrayList<Pair<Integer, String>> ret = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()){
            ret.add(new Pair<Integer, String>(cursor.getInt(cursor.getColumnIndex(SymptomManagerContract.Symptoms.SYMPTOM_ID)), cursor.getString(cursor.getColumnIndex(SymptomManagerContract.Symptoms.NAME))));
        }
        cursor.close();

        return ret;
    }

    public Boolean addSymptom(ContentResolver resolver, String name, String desc){
        ContentValues values = new ContentValues();
        values.put(SymptomManagerContract.Symptoms.NAME, name);
        values.put(SymptomManagerContract.Symptoms.DESCRIPTION, desc);

        resolver.insert(SymptomManagerContract.Symptoms.CONTENT_URI, values);
        return true;
    }

    public Boolean addEpisode(ContentResolver resolver, long datetime, int symptomId, float discomfort){
        ContentValues values = new ContentValues();
        values.put(SymptomManagerContract.Episodes.DATETIME, datetime);
        values.put(SymptomManagerContract.Episodes.SYMPTOM_ID, symptomId);
        values.put(SymptomManagerContract.Episodes.DISCOMFORT, discomfort);

        resolver.insert(SymptomManagerContract.Episodes.CONTENT_URI, values);

        return true;
    }

    public LinkedList<Pair<Long, Integer>> getLastNDaysEpisodes(ContentResolver resolver, int n, int id){
        long lastDate =  Calendar.getInstance().getTimeInMillis() - (86400000L * n);
        final String[] cols = {SymptomManagerContract.Episodes.DATETIME, SymptomManagerContract.Episodes.DISCOMFORT};
        Cursor cursor = resolver.query(Uri.withAppendedPath(SymptomManagerContract.Episodes.CONTENT_URI,"/"+SymptomManagerContract.Episodes.DATETIME+"/limit/"+Long.toString(lastDate)), cols, SymptomManagerContract.Episodes.SYMPTOM_ID + " = " + Integer.toString(id), null, null);
        LinkedList<Pair<Long, Integer>> retVal = new LinkedList<>();
        while (cursor.moveToNext()){
            retVal.add(new Pair<Long, Integer>(Double.doubleToLongBits((cursor.getDouble(cursor.getColumnIndex(SymptomManagerContract.Episodes.DATETIME)) - (Calendar.getInstance().getTimeInMillis() - (86400000L * n)))/86400000), cursor.getInt(cursor.getColumnIndex(SymptomManagerContract.Episodes.DISCOMFORT))));
        }
        cursor.close();

        return retVal;
    }
}
