package dev.ryan.tasker;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;

import dev.ryan.tasker.data.DbHelper;
import dev.ryan.tasker.data.TaskContract.Task;

/**
 * Created by ryan on 05/03/18.
 */

public class TaskListAdapter extends ArrayAdapter<DataModel> {
    public TaskListAdapter(Context context, ArrayList<DataModel> Task) {
        super(context, 0, Task);
    }

    long delay = 1000;
    long last_text_edit = 0;
    Handler handler = new Handler();

    String lastInput = "";

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.task_row_view, parent, false);
        }

        DbHelper mDbHelper = DbHelper.getInstance(getContext());
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final DataModel currentData = getItem(position);

        // Set the @task_line in @task_row_view
        final TextView taskTextView = listItemView.findViewById(R.id.task_line);
        taskTextView.setText(currentData.getTask());

        // Set the @time_line in @task_row_view
        final TextView timeTextView = listItemView.findViewById(R.id.time_line);
        timeTextView.setText(currentData.getTime());


        // Set the @description_line in @task_row_view
        final TextView descView = listItemView.findViewById(R.id.description_line);
        descView.setText(currentData.getDescription());


        // Runnable
        // Update the database TaskContract.TASK.COLUMN_NAME
        final EditText taskNameField = listItemView.findViewById(R.id.task_field);
        final Runnable input_finish_checker = new Runnable() {
            public void run() {
                String task = taskNameField.getText().toString();
                if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                    if (task != lastInput) {
                        ContentValues values = new ContentValues();
                        values.put(Task.COLUMN_TASK, task);

                        String[] _id = {currentData.getId() + ""};
                        getContext().getContentResolver().update(Task.BASE_CONTENT_URI, values, "_id=?", _id);

                        currentData.task = task;
                        lastInput = taskNameField.getText().toString();
                    }
                }
            }
        };

        // Runnable
        // Update the database TaskContract.TASK.COLUMN_DESCRIPTION
        final EditText taskDescField = listItemView.findViewById(R.id.desc_field);
        final Runnable desc_finish_checker = new Runnable() {
            public void run() {
                String desc = taskDescField.getText().toString();
                if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                    if (desc != lastInput) {
                        ContentValues values = new ContentValues();
                        values.put(Task.COLUMN_DESC, desc);

                        String[] _id = {currentData.getId() + ""};
                        getContext().getContentResolver().update(Task.BASE_CONTENT_URI, values, "_id=?", _id);

                        currentData.description = desc;
                        lastInput = taskNameField.getText().toString();
                    }
                }
            }
        };

        // TextWatcher for @task_field in @task_row_view
        // Automatically change @task_line when @task_field change
        // Call the task_finish_checker (RUNNABLE) after 1000ms
        TextWatcher taskNameWatcher = new TextWatcher() {
            public void afterTextChanged(final Editable s) {
                taskTextView.setText(s.toString());
                currentData.task = taskNameField.getText().toString();
                if (s.length() > 0) {
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, delay);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };
        taskNameField.addTextChangedListener(taskNameWatcher);

        // TextWatcher for @desc_field
        // Automatically change @description_line when @description_field change
        // Call the desc_finish_checker (RUNNABLE) after 3000ms
        TextWatcher taskDescWatcher = new TextWatcher() {
            /**
             * @param s : text from the @task_field
             */
            public void afterTextChanged(final Editable s) {
                descView.setText(s.toString());
                currentData.task = taskNameField.getText().toString();
                if (s.length() > 0) {
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(desc_finish_checker, delay);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };
        taskDescField.addTextChangedListener(taskDescWatcher);


        // OnClickListener to check @check_task_done (DONE/DO)
        // DO : Uncheck & show in TASK_TODO & TaskContract.Task.COLUMN_STATUS = 0
        // DONE : Checked & doesn't show up & TaskContract.Task.COLUMN_STATUS = 1
        final CheckBox taskCheckBox = listItemView.findViewById(R.id.check_task_done);
        taskCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (taskCheckBox.isChecked()) {
                    ContentValues values = new ContentValues();
                    String[] _id = {currentData.getId() + ""};
                    values.put(Task.COLUMN_STATUS, "1");
                    int id = db.update(Task.TABLE_NAME, values, "_id=?", _id);
                }
            }
        });

        // OnClickListener for @button_delete_list
        // Directly delete database row by _id
        final Button deleteButton = listItemView.findViewById(R.id.button_delete_list);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] id = {currentData.getId() + ""};
                String where = "_id=?";
                int status = getContext().getContentResolver().delete(Task.BASE_CONTENT_URI, where, id);
                Log.v("DELETE STATUS ", status + "");
            }
        });

        return listItemView;
    }


}