package com.odownard.symptomlogger.SymptomManager;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by olive_000 on 01/08/2015.
 * Defines the contract schema for the database
 */
public class SymptomManagerContract {

    public SymptomManagerContract(){

    }

    public static final class Symptoms implements BaseColumns {
        private Symptoms(){

        }

        public static final Uri CONTENT_URI = Uri.parse("content://" + SymptomManagerProvider.AUTHORITY + "/symptoms");
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.odownard.symptoms";
        public static final String SYMPTOM_ID = "_id";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String DISCOMFORT = "discomfort";
    }

    public static final class Episodes implements  BaseColumns {
        private Episodes () {}
        public static final Uri CONTENT_URI = Uri.parse("content://" + SymptomManagerProvider.AUTHORITY + "/episodes");
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.odownard.episodes";
        public static final String EPISODE_ID = "_id";
        public static final String DATETIME = "datetime";
        public static final String SYMPTOM_ID = "symptom_id";
    }
}
