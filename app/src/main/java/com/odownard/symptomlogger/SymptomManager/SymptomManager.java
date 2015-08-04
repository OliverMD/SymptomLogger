package com.odownard.symptomlogger.SymptomManager;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
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
}
