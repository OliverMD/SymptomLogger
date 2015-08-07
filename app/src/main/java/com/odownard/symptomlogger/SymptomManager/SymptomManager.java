package com.odownard.symptomlogger.SymptomManager;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
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

    public Boolean addSymptom(ContentResolver resolver, String name, String desc, int discomfort){
        ContentValues values = new ContentValues();
        values.put(SymptomManagerContract.Symptoms.NAME, name);
        values.put(SymptomManagerContract.Symptoms.DESCRIPTION, desc);
        values.put(SymptomManagerContract.Symptoms.DISCOMFORT, discomfort);

        resolver.insert(SymptomManagerContract.Symptoms.CONTENT_URI, values);
        return true;
    }

    public Boolean addEpisode(ContentResolver resolver, long datetime, int symptomId){
        ContentValues values = new ContentValues();
        values.put(SymptomManagerContract.Episodes.DATETIME, datetime);
        values.put(SymptomManagerContract.Episodes.SYMPTOM_ID, symptomId);

        resolver.insert(SymptomManagerContract.Episodes.CONTENT_URI, values);

        return true;
    }

    public LinkedList<DataPoint> getNEpisodes(ContentResolver resolver, int n){
        Cursor cursor = resolver.query(Uri.withAppendedPath(SymptomManagerContract.Symptoms.CONTENT_URI, "/" + SymptomManagerProvider.EPISODES_TABLE_NAME + "/" + Integer.toString(n)), null, null, null, null);
        LinkedList<DataPoint> retVal = new LinkedList<>();
        while (cursor.moveToNext()){
            retVal.add(new DataPoint(cursor.getDouble(cursor.getColumnIndex(SymptomManagerContract.Episodes.DATETIME)), cursor.getInt(cursor.getColumnIndex(SymptomManagerContract.Symptoms.DISCOMFORT))));
        }
        cursor.close();

        return retVal;
    }
}
