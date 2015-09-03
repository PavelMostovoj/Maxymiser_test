package com.example.Maxymiser_test;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.Calendar;


public class SelectFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    final static String LOG_TAG = "Select Fragment";

    ListView List;
    private ListAdapter mListAdapter;

    SQLiteHelper sqLiteHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_select, container, false);

        sqLiteHelper = SQLiteHelper.getInstance(getActivity());

        mListAdapter = new ListAdapter(getActivity());
        List = (ListView)rootView.findViewById(R.id.List);
        List.setAdapter(mListAdapter);
        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Intent intent = new Intent(parent.getContext(), DetailActivity.class);

                long idfromDB = (Long)view.findViewById(R.id.TV_id).getTag();

                intent.putExtra("ID", String.valueOf(idfromDB));
                startActivity(intent);
            }
        });

        // создаем лоадер для чтения данных
        getActivity().getSupportLoaderManager().initLoader(0, null, this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(LOG_TAG, " onResume");
        getActivity().getSupportLoaderManager().getLoader(0).forceLoad();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, " onPause");
    }


    private class ListAdapter extends BaseAdapter {

        Context ctx;
        Cursor cursor;
        private LayoutInflater mInflator;


        public ListAdapter(Context context) {
            super();
            ctx = context;
            mInflator = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void swapCursor(Cursor cursor) {
            this.cursor = cursor;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (cursor == null) return 0;
            return cursor.getCount();
        }

        @Override
        public Object getItem(int position) {

            if (cursor.moveToPosition(position)) {

                int idColIndex = cursor.getColumnIndex(SQLiteHelper.COLUMN_ID);
                int datatimeColIndex = cursor.getColumnIndex(SQLiteHelper.COLUMN_DATA_TIME);
                int todoNameColIndex = cursor.getColumnIndex(SQLiteHelper.COLUMN_TODO_NAME);
                int todoEntryColIndex = cursor.getColumnIndex(SQLiteHelper.COLUMN_TODO_ENTRY);

                return new TODOEntry(cursor.getLong(idColIndex),
                        cursor.getLong(datatimeColIndex),
                        cursor.getString(todoNameColIndex),
                        cursor.getString(todoEntryColIndex));

            } else {
                return null;
            }
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public long getItemIdFromDB(int position) {
            if(cursor.moveToPosition(position)){
                int idColIndex = cursor.getColumnIndex(SQLiteHelper.COLUMN_ID);
                return cursor.getInt(idColIndex);
            }

            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;

            if (view == null) {
                view = mInflator.inflate(R.layout.list_listitem, null);

                viewHolder = new ViewHolder();
                viewHolder.idTv = (TextView) view.findViewById(R.id.TV_id);
                viewHolder.dataTimeTV = (TextView) view.findViewById(R.id.TV_DatatTime);
                viewHolder.todoNameTV = (TextView) view.findViewById(R.id.TV_ToDoName);
                viewHolder.todoEntryTV = (TextView) view.findViewById(R.id.TV_ToDoEntry);

                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            TODOEntry tempTODOEntry = (TODOEntry)getItem(i);
            if(tempTODOEntry != null)
            {
                viewHolder.idTv.setText(String.valueOf(tempTODOEntry.getId()));
                viewHolder.idTv.setTag(tempTODOEntry.getId());
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(tempTODOEntry.getDatatime());
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                viewHolder.dataTimeTV.setText("" + mYear +"."+  mMonth +"."+ mDay);
                viewHolder.todoNameTV.setText(tempTODOEntry.getTodoName());
                viewHolder.todoEntryTV.setText(tempTODOEntry.getTodoEntry());
            }

            return view;
        }
    }


    static class ViewHolder {
        TextView idTv;
        TextView dataTimeTV;
        TextView todoNameTV;
        TextView todoEntryTV;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        return new MyCursorLoader(getActivity(), sqLiteHelper);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mListAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }



    static class MyCursorLoader extends CursorLoader {

        SQLiteHelper db;

        public MyCursorLoader(Context context, SQLiteHelper db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.getAllData();

            return cursor;
        }
    }



}

