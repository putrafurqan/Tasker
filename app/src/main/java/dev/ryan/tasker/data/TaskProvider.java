package dev.ryan.tasker.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import dev.ryan.tasker.data.TaskContract.Task;
import android.util.Log;

/**
 * Created by ryan on 25/02/18.
 */

public class TaskProvider extends ContentProvider {
    private static final int TASK = 200;

    //URI matcher code for single content in table
    private static final int TASK_ID = 201;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(Task.CONTENT_AUTHORITY, Task.PATH_TASKS, TASK);
        sUriMatcher.addURI(Task.CONTENT_AUTHORITY,Task.PATH_TASKS + "/#", TASK_ID);
    }

    public static final String LOG_TAG = TaskProvider.class.getSimpleName();
    private DbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = DbHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String s1) {
        DbHelper mDbHelper = new DbHelper(getContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                Task.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                TaskContract.Task.COLUMN_PRIORITY);;
        return cursor;
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String name = values.getAsString(Task.COLUMN_TASK);
        if (name == null) {
            throw new IllegalArgumentException("Task requires a name");
        }
        insertTask(uri, values);
        return null;

    }

    public Uri insertTask(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id = db.insert(Task.TABLE_NAME, null, values);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String where, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int id = db.delete(Task.TABLE_NAME, where, selectionArgs);
        return id;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        //Inspection
        String[] _id = {values.getAsString(Task.COLUMN_ID)};
        String name = values.getAsString(Task.COLUMN_TASK);
        String time = values.getAsString(Task.COLUMN_TIME);
        String desc = values.getAsString(Task.COLUMN_DESC);
        String prio = values.getAsString(Task.COLUMN_PRIORITY);

        String[] projection = {
                Task.COLUMN_ID,
                Task.COLUMN_TASK,
                Task.COLUMN_TIME,
                Task.COLUMN_DESC
        };

        int id = db.update(Task.TABLE_NAME, values, where, whereArgs);
        return id;
    }
}
