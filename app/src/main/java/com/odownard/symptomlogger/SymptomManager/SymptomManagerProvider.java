package com.odownard.symptomlogger.SymptomManager;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by olive_000 on 01/08/2015.
 * The content provider used for interacting with database
 */
public class SymptomManagerProvider extends ContentProvider {

    private static final String TAG = "SymptomManagerContentProvider";
    private static final String DATABASE_NAME = "SymptomManager.db";
    private static final int DATABASE_VERSION = 4;
    protected static final String SYMPTOMS_TABLE_NAME = "symptoms";
    protected static final String EPISODES_TABLE_NAME = "episodes";
    public static final String AUTHORITY = "com.odownard.symptomlogger.providers.SymptomManagerProvider";
    private static final UriMatcher sUriMatcher;
    private static final int SYMPTOMS = 1;
    private static final int SYMPTOMS_ID = 2;
    private static final int EPISODES = 3;
    private static final int EPISODES_ID = 4;
    private static final int EPISODE_SYMPTOM_JOIN = 5;
    private static HashMap<String, String> mProjectionMap;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, SYMPTOMS_TABLE_NAME, SYMPTOMS);
        sUriMatcher.addURI(AUTHORITY, SYMPTOMS_TABLE_NAME + "/#", SYMPTOMS_ID);
        sUriMatcher.addURI(AUTHORITY, EPISODES_TABLE_NAME, EPISODES);
        sUriMatcher.addURI(AUTHORITY, EPISODES_TABLE_NAME + "/#", EPISODES_ID);
        sUriMatcher.addURI(AUTHORITY, EPISODES_TABLE_NAME + "/" + SYMPTOMS_TABLE_NAME+"/#", EPISODE_SYMPTOM_JOIN);
        sUriMatcher.addURI(AUTHORITY, SYMPTOMS_TABLE_NAME + "/" + EPISODES_TABLE_NAME+"/#", EPISODE_SYMPTOM_JOIN);

        mProjectionMap = new HashMap<>();
        mProjectionMap.put(SymptomManagerContract.Symptoms.SYMPTOM_ID, SymptomManagerContract.Symptoms.SYMPTOM_ID);
        mProjectionMap.put(SymptomManagerContract.Symptoms.NAME, SymptomManagerContract.Symptoms.NAME);
        mProjectionMap.put(SymptomManagerContract.Symptoms.DESCRIPTION, SymptomManagerContract.Symptoms.DESCRIPTION);
        mProjectionMap.put(SymptomManagerContract.Symptoms.DISCOMFORT, SymptomManagerContract.Symptoms.DISCOMFORT);

        mProjectionMap.put(SymptomManagerContract.Episodes.EPISODE_ID, SymptomManagerContract.Episodes.EPISODE_ID);
        mProjectionMap.put(SymptomManagerContract.Episodes.DATETIME, SymptomManagerContract.Episodes.DATETIME);
        mProjectionMap.put(SymptomManagerContract.Episodes.SYMPTOM_ID, SymptomManagerContract.Episodes.SYMPTOM_ID);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            //Create the symptoms table
            db.execSQL("CREATE TABLE " + SYMPTOMS_TABLE_NAME + "( " + SymptomManagerContract.Symptoms.SYMPTOM_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + SymptomManagerContract.Symptoms.NAME + " VARCHAR(255), " +
                    SymptomManagerContract.Symptoms.DESCRIPTION + " LONGTEXT, " + SymptomManagerContract.Symptoms.DISCOMFORT +
            " INTEGER);");

            //Create episodes table
            db.execSQL("CREATE TABLE " + EPISODES_TABLE_NAME + "( " + SymptomManagerContract.Episodes.EPISODE_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + SymptomManagerContract.Episodes.DATETIME + " INTEGER, " +
                    SymptomManagerContract.Episodes.SYMPTOM_ID + " INTEGER, FOREIGN KEY(" + SymptomManagerContract.Episodes.SYMPTOM_ID +
            ") REFERENCES " + SYMPTOMS_TABLE_NAME + "(" + SymptomManagerContract.Symptoms.SYMPTOM_ID + "));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+ SYMPTOMS_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS "+ EPISODES_TABLE_NAME);
            onCreate(db);
        }
    }

    private DatabaseHelper dbHelper;


    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(SYMPTOMS_TABLE_NAME + ", " + EPISODES_TABLE_NAME);
        qb.setProjectionMap(mProjectionMap);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        switch (sUriMatcher.match(uri)) {

            case SYMPTOMS:
                qb.setTables(SYMPTOMS_TABLE_NAME);
                break;
            case SYMPTOMS_ID:
                qb.setTables(SYMPTOMS_TABLE_NAME);
                selection = selection + "_id = " + uri.getLastPathSegment();
                break;
            case EPISODES:
                qb.setTables(EPISODES_TABLE_NAME);
                break;
            case EPISODES_ID:
                qb.setTables(EPISODES_TABLE_NAME);
                selection = selection + "_id = " + uri.getLastPathSegment();
                break;
            case EPISODE_SYMPTOM_JOIN:
                /**
                final String queryString = "SELECT " + SymptomManagerContract.Episodes.DATETIME + ", " + SymptomManagerContract.Symptoms.DISCOMFORT
                        + " FROM "
                        +"( "
                        + " SELECT TOP (10) * "
                        + " FROM " + SymptomManagerProvider.SYMPTOMS_TABLE_NAME + " AS S INNER JOIN " + SymptomManagerProvider.EPISODES_TABLE_NAME + " AS E "
                        + " ON S." + SymptomManagerContract.Symptoms.SYMPTOM_ID + "=E." + SymptomManagerContract.Episodes.SYMPTOM_ID
                        + " ORDER BY " + SymptomManagerContract.Episodes.DATETIME + " ASC"
                        + ") "
                        + "ORDER BY " + SymptomManagerContract.Episodes.DATETIME + " DESC;";
                 **/

                final String queryString = "SELECT datetime, discomfort FROM(  SELECT * FROM symptoms AS S INNER JOIN episodes AS E ON S._id=E.symptom_id ORDER BY datetime ASC LIMIT 10) ORDER BY datetime ASC;";
                return db.rawQuery(queryString,new String[]{});
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri);
        }

        return qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case SYMPTOMS:
                return SymptomManagerContract.Symptoms.CONTENT_TYPE;
            case EPISODES:
                return SymptomManagerContract.Episodes.CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        ContentValues values;
        if (initialValues != null){
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        long rowId;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)){
            case SYMPTOMS:
                //Symptoms.DESCRIPTION is a nullable column, required for db.insert
                rowId = db.insert(SYMPTOMS_TABLE_NAME, SymptomManagerContract.Symptoms.DESCRIPTION, values);
                if (rowId > 0){
                    Uri symptomUri = ContentUris.withAppendedId(SymptomManagerContract.Symptoms.CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(symptomUri, null);
                    return symptomUri;
                }
            case EPISODES:
                rowId = db.insert(EPISODES_TABLE_NAME, SymptomManagerContract.Episodes.SYMPTOM_ID, values);
                if (rowId > 0) {
                    Uri episodeUri = ContentUris.withAppendedId(SymptomManagerContract.Episodes.CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(episodeUri, null);
                    return episodeUri;
                }
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri);

        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String table;
        switch (sUriMatcher.match(uri)){
            case EPISODES:
                table = EPISODES_TABLE_NAME;
                break;
            case EPISODES_ID:
                table = EPISODES_TABLE_NAME;
                selection = selection + "_id = " + uri.getLastPathSegment();
                break;
            case SYMPTOMS:
                table = SYMPTOMS_TABLE_NAME;
                break;
            case SYMPTOMS_ID:
                table = SYMPTOMS_TABLE_NAME;
                selection = selection + "_id = " + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri "+uri);
        }

        int count = db.delete(table, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch(sUriMatcher.match(uri)){
            case SYMPTOMS:
                count = db.update(SYMPTOMS_TABLE_NAME, values, selection, selectionArgs);
                break;
            case EPISODES:
                count = db.update(EPISODES_TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri "+uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
