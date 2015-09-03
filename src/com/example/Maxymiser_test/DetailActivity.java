package com.example.Maxymiser_test;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class DetailActivity extends Activity{

    final static String LOG_TAG = "Detail Activity";

    SQLiteHelper sqLiteHelper;

    //GUI
    TextView idTV;
    TextView dataTimeTV;
    TextView todoNameTV;
    TextView todoEntryTV;
    Button deleteButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        sqLiteHelper = SQLiteHelper.getInstance(this);

        final String id = getIntent().getStringExtra("ID");

        idTV = (TextView) findViewById(R.id.DetailActivity_id);
        dataTimeTV = (TextView) findViewById(R.id.DetailActivity_dataTime);
        todoNameTV = (TextView) findViewById(R.id.DetailActivity_todoName);
        todoEntryTV = (TextView) findViewById(R.id.DetailActivity_todoEntry);
        deleteButton = (Button) findViewById(R.id.delete);

        idTV.setText(id);
        dataTimeTV.setText(getResources().getString(R.string.datatime));
        todoNameTV.setText(getResources().getString(R.string.todoName));
        todoEntryTV.setText(getResources().getString(R.string.todoEntry));

        Cursor cursor = sqLiteHelper.getRec(id);

        Log.d(LOG_TAG, " cursor.getCount() = " + cursor.getCount());

        if (cursor.moveToFirst()) {

            int datatimeColIndex = cursor.getColumnIndex(SQLiteHelper.COLUMN_DATA_TIME);
            int todoNameColIndex = cursor.getColumnIndex(SQLiteHelper.COLUMN_TODO_NAME);
            int todoEntryColIndex = cursor.getColumnIndex(SQLiteHelper.COLUMN_TODO_ENTRY);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(cursor.getLong(datatimeColIndex));
            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);
            dataTimeTV.setText("" + mYear +"."+  mMonth +"."+ mDay);
            todoNameTV.setText(cursor.getString(todoNameColIndex));
            todoEntryTV.setText(cursor.getString(todoEntryColIndex));

        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteHelper.delRec(Integer.parseInt(id));
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
