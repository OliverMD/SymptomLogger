package com.odownard.symptomlogger.DataManager;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by oliver downard on 01/08/2015.
 * Defines the contract schema for the database
 */
public class DataManagerContract {

    public DataManagerContract(){

    }

    public static final class Symptoms implements BaseColumns {
        private Symptoms(){}

        public static final Uri CONTENT_URI = Uri.parse("content://" + DataManagerProvider.AUTHORITY + "/symptoms");
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.odownard.symptoms";
        public static final String SYMPTOM_ID = "_id";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
    }

    public static final class Episodes implements  BaseColumns {
        private Episodes () {}
        public static final Uri CONTENT_URI = Uri.parse("content://" + DataManagerProvider.AUTHORITY + "/episodes");
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.odownard.episodes";
        public static final String EPISODE_ID = "_id";
        public static final String DATETIME = "datetime";
        public static final String DISCOMFORT = "discomfort";
        public static final String SYMPTOM_ID = "symptom_id";
    }

    public static final class Tags implements BaseColumns {
        private Tags(){}

        public static final Uri CONTENT_URI = Uri.parse("content://"+DataManagerProvider.AUTHORITY + "/tags");
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.odownard.tags";
        public static final String TAG_ID = "_id";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
    }

    public static final class TagEpisodes implements BaseColumns {
        private TagEpisodes(){}

        public static final Uri CONTENT_URI = Uri.parse("content://"+DataManagerProvider.AUTHORITY+"/tag_episodes");
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.odownard.tagepisodes";
        public static final String TAG_EPISODE_ID = "_id";
        public static final String DATETIME = "datetime";
        public static final String TAG_ID = "tag_id";
    }
}
