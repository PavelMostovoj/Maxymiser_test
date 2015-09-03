package com.example.Maxymiser_test;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;


public class InsertFragment extends Fragment {

    final static String LOG_TAG = "Insert Fragment";

    SQLiteHelper sqLiteHelper;

    // GUI
    EditText ToDoNameET;
    EditText ToDoEntryET;
    DatePicker datePicker;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.fragment_insert, container, false);

        sqLiteHelper = SQLiteHelper.getInstance(getActivity());

        ToDoNameET = (EditText)rootView.findViewById(R.id.ET_todoName);
        ToDoEntryET = (EditText)rootView.findViewById(R.id.ET_todoEntry);
        datePicker = (DatePicker)rootView.findViewById(R.id.datePicker);

        Button readbtn = (Button)rootView.findViewById(R.id.read);
        readbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor c = sqLiteHelper.getAllData();

                if (c.moveToFirst()) {

                    int idColIndex = c.getColumnIndex(SQLiteHelper.COLUMN_ID);
                    int datatimeColIndex = c.getColumnIndex(SQLiteHelper.COLUMN_DATA_TIME);
                    int todoNameColIndex = c.getColumnIndex(SQLiteHelper.COLUMN_TODO_NAME);
                    int todoEntryColIndex = c.getColumnIndex(SQLiteHelper.COLUMN_TODO_ENTRY);


                    do {
                        Log.d(LOG_TAG,
                                "COLUMN_ID = " + c.getInt(idColIndex) +
                                        ", COLUMN_DATA_TIME = " + c.getString(datatimeColIndex) +
                                        ", COLUMN_TODO_NAME = " + c.getString(todoNameColIndex) +
                                        ", COLUMN_TODO_ENTRY = " + c.getString(todoEntryColIndex)
                                        );

                    } while (c.moveToNext());

                } else {
                    Log.d(LOG_TAG, "0 rows");
                }
                c.close();
            }
        });

        Button insertbtn = (Button) rootView.findViewById(R.id.insert);
        insertbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean allRowstrue = true;
                long datatime;

                String todonamestr = ToDoNameET.getText().toString();
                String todoentrystr = ToDoEntryET.getText().toString();

                if( todonamestr.length() == 0 ) {
                    allRowstrue = false;
                    Toast.makeText(getActivity(), getResources().getString(R.string.input_todo_name), Toast.LENGTH_SHORT).show();
                }
                if( todoentrystr.length() == 0 ){
                    allRowstrue = false;
                    Toast.makeText(getActivity(), getResources().getString(R.string.input_todo_entry), Toast.LENGTH_SHORT).show();
                }

                Calendar calendar = Calendar.getInstance();
                //calendar.set(datePicker.getYear(), Calendar.JANUARY, datePicker.getDayOfMonth());
                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                datatime = calendar.getTimeInMillis();


                if (allRowstrue){
                    sqLiteHelper.addRec(datatime, todonamestr, todoentrystr);
                    Toast.makeText(getActivity(),getResources().getString(R.string.input_success),Toast.LENGTH_SHORT).show();
                    getActivity().getSupportLoaderManager().getLoader(0).forceLoad();
                }


            }
        });


        datePicker.setCalendarViewShown(false);

        return rootView;
    }

}
