package dev.ryan.tasker.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ryan on 25/02/18.
 */
public class TaskContract implements BaseColumns {
    public static class Task {
        public static final String CONTENT_AUTHORITY = "dev.ryan.tasker";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH_TASKS = "tasks";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TASKS);
        public static final String DATABASE_NAME = "TaskData.db";
        public static final int DATABASE_VERSION = 1;
        public static final String TABLE_NAME = "task";
        public static final String COLUMN_TASK = "task";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_DESC = "description";
        public static final String COLUMN_PRIORITY = "priority";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_ID = "_id";

        public static final class TaskEntry implements BaseColumns {
            /**
             * The content URI to access the pe t data in the provider
             */
            public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TASKS);
            /**
             * Name of database table for pets
             */
            public final static String TABLE_NAME = "task";
        }
    }
}
