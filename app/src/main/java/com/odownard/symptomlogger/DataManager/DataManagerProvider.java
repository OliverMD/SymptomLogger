package com.odownard.symptomlogger.DataManager;

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
import android.nfc.Tag;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by olive_000 on 01/08/2015.
 * The content provider used for interacting with database
 */
public class DataManagerProvider extends ContentProvider {

    private static final String TAG = "DataManagerContentProvider";
    private static final String DATABASE_NAME = "DataManager.db";
    private static final int DATABASE_VERSION = 9;
    public static final String SYMPTOMS_TABLE_NAME = "symptoms";
    public static final String EPISODES_TABLE_NAME = "episodes";
    public static final String TAG_TABLE_NAME = "tags";
    public static final String TAG_EPISODES_TABLE_NAME = "tag_episodes";
    public static final String AUTHORITY = "com.odownard.symptomlogger.providers.DataManagerProvider";
    private static final UriMatcher sUriMatcher;
    private static final int SYMPTOMS = 1;
    private static final int SYMPTOMS_ID = 2;
    private static final int EPISODES = 3;
    private static final int EPISODES_ID = 4;
    private static final int EPISODE_DATETIME_LIM = 5;
    private static final int HOME_ELEMENTS = 6;
    private static final int TAGS = 7;
    private static final int TAGS_ID = 8;
    private static final int TAG_EPISODES = 9;
    private static final int TAG_EPISODES_ID = 10;
    private static HashMap<String, String> mProjectionMap;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, SYMPTOMS_TABLE_NAME, SYMPTOMS);
        sUriMatcher.addURI(AUTHORITY, SYMPTOMS_TABLE_NAME + "/#", SYMPTOMS_ID);
        sUriMatcher.addURI(AUTHORITY, EPISODES_TABLE_NAME, EPISODES);
        sUriMatcher.addURI(AUTHORITY, EPISODES_TABLE_NAME + "/#", EPISODES_ID);
        sUriMatcher.addURI(AUTHORITY, EPISODES_TABLE_NAME + "/"+ DataManagerContract.Episodes.DATETIME+"/limit/#",EPISODE_DATETIME_LIM);
        sUriMatcher.addURI(AUTHORITY, "home-elements", HOME_ELEMENTS);
        sUriMatcher.addURI(AUTHORITY, TAG_TABLE_NAME, TAGS);
        sUriMatcher.addURI(AUTHORITY, TAG_TABLE_NAME + "/#", TAGS_ID);
        sUriMatcher.addURI(AUTHORITY, TAG_EPISODES_TABLE_NAME, TAG_EPISODES);
        sUriMatcher.addURI(AUTHORITY, TAG_EPISODES_TABLE_NAME + "/#", TAG_EPISODES_ID);
        Log.v("Data Provider", sUriMatcher.toString());

        mProjectionMap = new HashMap<>();
        mProjectionMap.put(DataManagerContract.Symptoms.SYMPTOM_ID, DataManagerContract.Symptoms.SYMPTOM_ID);
        mProjectionMap.put(DataManagerContract.Symptoms.NAME, DataManagerContract.Symptoms.NAME);
        mProjectionMap.put(DataManagerContract.Symptoms.DESCRIPTION, DataManagerContract.Symptoms.DESCRIPTION);

        mProjectionMap.put(DataManagerContract.Episodes.DISCOMFORT, DataManagerContract.Episodes.DISCOMFORT);
        mProjectionMap.put(DataManagerContract.Episodes.EPISODE_ID, DataManagerContract.Episodes.EPISODE_ID);
        mProjectionMap.put(DataManagerContract.Episodes.DATETIME, DataManagerContract.Episodes.DATETIME);
        mProjectionMap.put(DataManagerContract.Episodes.SYMPTOM_ID, DataManagerContract.Episodes.SYMPTOM_ID);

        mProjectionMap.put(DataManagerContract.Tags.TAG_ID, DataManagerContract.Tags.TAG_ID);
        mProjectionMap.put(DataManagerContract.Tags.NAME, DataManagerContract.Tags.NAME);
        mProjectionMap.put(DataManagerContract.Tags.DESCRIPTION, DataManagerContract.Tags.DESCRIPTION);

        mProjectionMap.put(DataManagerContract.TagEpisodes.TAG_EPISODE_ID, DataManagerContract.TagEpisodes.TAG_EPISODE_ID);
        mProjectionMap.put(DataManagerContract.TagEpisodes.DATETIME, DataManagerContract.TagEpisodes.DATETIME);
        mProjectionMap.put(DataManagerContract.TagEpisodes.TAG_ID, DataManagerContract.TagEpisodes.TAG_ID);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            //Create the symptoms table
            db.execSQL("CREATE TABLE " + SYMPTOMS_TABLE_NAME + "( " + DataManagerContract.Symptoms.SYMPTOM_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + DataManagerContract.Symptoms.NAME + " VARCHAR(255), " +
                    DataManagerContract.Symptoms.DESCRIPTION + " LONGTEXT);");

            //Create episodes table
            db.execSQL("CREATE TABLE " + EPISODES_TABLE_NAME + "( " + DataManagerContract.Episodes.EPISODE_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + DataManagerContract.Episodes.DATETIME + " INTEGER, " +
                            DataManagerContract.Episodes.DISCOMFORT + " REAL, "+
                    DataManagerContract.Episodes.SYMPTOM_ID + " INTEGER, FOREIGN KEY(" + DataManagerContract.Episodes.SYMPTOM_ID +
            ") REFERENCES " + SYMPTOMS_TABLE_NAME + "(" + DataManagerContract.Symptoms.SYMPTOM_ID + "));");

            db.execSQL("CREATE TABLE " + TAG_TABLE_NAME + "( " + DataManagerContract.Tags.TAG_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + DataManagerContract.Tags.NAME + " VARCHAR(255), " +
                    DataManagerContract.Tags.DESCRIPTION + " LONGTEXT);");

            db.execSQL("CREATE TABLE " + TAG_EPISODES_TABLE_NAME + "( " + DataManagerContract.TagEpisodes.TAG_EPISODE_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + DataManagerContract.TagEpisodes.DATETIME + " INTEGER, " +
                    DataManagerContract.TagEpisodes.TAG_ID + " INTEGER, FOREIGN KEY(" + DataManagerContract.TagEpisodes.TAG_ID+
            ") REFERENCES " + TAG_TABLE_NAME + "(" + DataManagerContract.Tags.TAG_ID+"));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+ SYMPTOMS_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + EPISODES_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS "+ TAG_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TAG_EPISODES_TABLE_NAME);
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
        qb.setProjectionMap(mProjectionMap);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        switch (sUriMatcher.match(uri)) {

            case SYMPTOMS:
                qb.setTables(SYMPTOMS_TABLE_NAME);
                break;
            case SYMPTOMS_ID:
                qb.setTables(SYMPTOMS_TABLE_NAME);
                selection = "_id = " + uri.getLastPathSegment();
                break;
            case EPISODES:
                qb.setTables(EPISODES_TABLE_NAME);
                break;
            case EPISODES_ID:
                qb.setTables(EPISODES_TABLE_NAME);
                selection = "_id = " + uri.getLastPathSegment();
                break;
            case EPISODE_DATETIME_LIM:
                qb.setTables(EPISODES_TABLE_NAME);
                if (selection == null) {
                    selection = DataManagerContract.Episodes.DATETIME + " > " + uri.getLastPathSegment();
                } else {
                    selection += " AND " + DataManagerContract.Episodes.DATETIME + " > " + uri.getLastPathSegment();
                }
                sortOrder = DataManagerContract.Episodes.DATETIME + " ASC";
                break;
            case HOME_ELEMENTS:
                String queryString = "SELECT " + DataManagerContract.Symptoms.SYMPTOM_ID + ", " + DataManagerContract.Symptoms.NAME + ", " + DataManagerContract.Symptoms.DESCRIPTION
                        + ", 'symptoms' AS source FROM " + SYMPTOMS_TABLE_NAME + " UNION ALL SELECT " + DataManagerContract.Tags.TAG_ID + ", " + DataManagerContract.Tags.NAME
                        + ", " + DataManagerContract.Tags.DESCRIPTION + ", 'tags' AS source FROM " + TAG_TABLE_NAME
                        + " ORDER BY " + DataManagerContract.Tags.DESCRIPTION;
                return db.rawQuery(queryString, null);
            case TAGS:
                qb.setTables(TAG_TABLE_NAME);
                break;
            case TAGS_ID:
                qb.setTables(TAG_TABLE_NAME);
                selection = "_id = " + uri.getLastPathSegment();
                break;
            case TAG_EPISODES:
                qb.setTables(TAG_EPISODES_TABLE_NAME);
                break;
            case TAG_EPISODES_ID:
                qb.setTables(TAG_EPISODES_TABLE_NAME);
                selection = "_id = " + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri);
        }
        return qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case SYMPTOMS:
                return DataManagerContract.Symptoms.CONTENT_TYPE;
            case EPISODES:
                return DataManagerContract.Episodes.CONTENT_TYPE;
            case TAGS:
                return DataManagerContract.Tags.CONTENT_TYPE;
            case TAG_EPISODES:
                return DataManagerContract.TagEpisodes.CONTENT_TYPE;
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
                rowId = db.insert(SYMPTOMS_TABLE_NAME, DataManagerContract.Symptoms.DESCRIPTION, values);
                if (rowId > 0){
                    Uri symptomUri = ContentUris.withAppendedId(DataManagerContract.Symptoms.CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(symptomUri, null);
                    return symptomUri;
                }
            case EPISODES:
                rowId = db.insert(EPISODES_TABLE_NAME, DataManagerContract.Episodes.SYMPTOM_ID, values);
                if (rowId > 0) {
                    Uri episodeUri = ContentUris.withAppendedId(DataManagerContract.Episodes.CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(episodeUri, null);
                    return episodeUri;
                }
            case TAGS:
                rowId = db.insert(TAG_TABLE_NAME, DataManagerContract.Tags.DESCRIPTION, values);
                if (rowId > 0){
                    Uri tagUri = ContentUris.withAppendedId(DataManagerContract.Tags.CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(tagUri, null);
                    return tagUri;
                }
            case TAG_EPISODES:
                rowId = db.insert(TAG_EPISODES_TABLE_NAME, DataManagerContract.TagEpisodes.TAG_EPISODE_ID, values);
                if (rowId > 0){
                    Uri tagEpisodeUri = ContentUris.withAppendedId(DataManagerContract.TagEpisodes.CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(tagEpisodeUri, null);
                    return tagEpisodeUri;
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
                selection = "_id = " + uri.getLastPathSegment();
                break;
            case SYMPTOMS:
                table = SYMPTOMS_TABLE_NAME;
                break;
            case SYMPTOMS_ID:
                table = SYMPTOMS_TABLE_NAME;
                selection = "_id = " + uri.getLastPathSegment();
                break;
            case TAGS:
                table = TAG_TABLE_NAME;
                break;
            case TAGS_ID:
                table = TAG_TABLE_NAME;
                selection = "_id = " + uri.getLastPathSegment();
                break;
            case TAG_EPISODES:
                table = TAG_EPISODES_TABLE_NAME;
                break;
            case TAG_EPISODES_ID:
                table = TAG_EPISODES_TABLE_NAME;
                selection = "_id = " + uri.getLastPathSegment();
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
