package dev.ryan.tasker;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import dev.ryan.tasker.data.TaskContract.Task;

import dev.ryan.tasker.data.DbHelper;

public class ViewActivity extends AppCompatActivity {
    //Test Comment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        getData();
    }
        int newData = 0;

        public void intentEdit(View view){
            insertNewData();
            getData();
        }
        public void getData() {
            ArrayList<DataModel> task = new ArrayList<DataModel>();
            String[] projection = {
                    Task.COLUMN_ID,
                    Task.COLUMN_TASK,
                    Task.COLUMN_TIME,
                    Task.COLUMN_DESC
            };
            String selectBy = Task.COLUMN_STATUS + "=?";
            String[] statusArg = checkDone();
            Cursor cursor = getContentResolver().query(Task.BASE_CONTENT_URI, projection,
                    selectBy, statusArg, Task.COLUMN_PRIORITY);

            try {
                int idColumnIndex = cursor.getColumnIndex(Task.COLUMN_ID);
                int nameColumnIndex = cursor.getColumnIndex(Task.COLUMN_TASK);
                int timeColumnIndex = cursor.getColumnIndex(Task.COLUMN_TIME);
                int descColumnIndex = cursor.getColumnIndex(Task.COLUMN_DESC);

                while (cursor.moveToNext()) {
                    int currentId = cursor.getInt(idColumnIndex);
                    String currentName = cursor.getString(nameColumnIndex);
                    String currentTime = cursor.getString(timeColumnIndex);
                    String currentDesc = cursor.getString(descColumnIndex);
                    DataModel taskClass = new DataModel(currentId, currentName, currentTime, currentDesc);
                    task.add(taskClass);

                }
            } finally {
                cursor.close();
                displayData(task);
            }

        }

    public void displayData(ArrayList<DataModel> task){
        ListView taskList = findViewById(R.id.task_list);
        TaskListAdapter itemsAdapter = new TaskListAdapter(this, task);
        taskList.setAdapter(itemsAdapter);
    }

    private void insertNewData() {
        ContentValues values = new ContentValues();
        values.put(Task.COLUMN_TASK, "New Task");
        values.put(Task.COLUMN_TIME, "00:00");
        values.put(Task.COLUMN_DESC, "-");
        values.put(Task.COLUMN_PRIORITY, "1");
        values.put(Task.COLUMN_STATUS, "0");
        newData +=1;
        Log.v("New Task",  newData + "");
        getContentResolver().insert(Task.BASE_CONTENT_URI, values);
    }

    // DO : VIEW IN TASK_TODO & STATUS = 0
    // DONE : DOESN'T SHOW UP & STATUS = 1
    private String[] checkDone(){
        CheckBox viewTaskDone = findViewById(R.id.view_task_done);
        if (!viewTaskDone.isChecked()) {
            return new String[]{"0"};
        }else {
            return new String[]{"1"};
        }
    }
}
