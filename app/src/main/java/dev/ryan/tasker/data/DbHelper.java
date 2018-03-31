package dev.ryan.tasker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import dev.ryan.tasker.ViewActivity;
import dev.ryan.tasker.data.TaskContract.Task;



/**
 * Created by ryan on 25/02/18.
 */

public class DbHelper extends SQLiteOpenHelper{
    public DbHelper(Context context) {
        super(context, Task.DATABASE_NAME, null, Task.DATABASE_VERSION);
    }
    private static DbHelper mInstance = null;

    public static DbHelper getInstance(Context ctx) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new DbHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_ENTRIES = " CREATE TABLE " + Task.TABLE_NAME + " ("
                + Task.COLUMN_ID + " INTEGER PRIMARY KEY, "
                + Task.COLUMN_TASK + " STRING NOT NULL, "
                + Task.COLUMN_TIME + " STRING NOT NULL, "
                + Task.COLUMN_DESC + " STRING, "
                + Task.COLUMN_PRIORITY + " INTEGER NOT NULL,"
                + Task.COLUMN_STATUS + " INTEGER NOT NULL"
                + ");";
        db.execSQL(CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("");
    }
}
