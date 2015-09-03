package com.example.Maxymiser_test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper {

    private static volatile SQLiteHelper instance;

    static final String LOG_TAG = "SQLite Helper";
    private final Context mCtx;

    static final String DB_NAME = "DBSqliteToDOTable";
    static final int    DB_VERSION = 1;
    static final String DB_TABLE_NAME = "ToDoTable";

    static final String COLUMN_ID = "id";
    static final String COLUMN_DATA_TIME = "data_time";
    static final String COLUMN_TODO_NAME = "todo_name";
    static final String COLUMN_TODO_ENTRY = "todo_entry";

    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    boolean open = false;

    private SQLiteHelper(Context ctx) {
        mCtx = ctx;
    }

    public static SQLiteHelper getInstance(Context ctx) {
        if(instance == null)
            synchronized (SQLiteHelper.class){
                if (instance == null)
                    instance = new SQLiteHelper(ctx);
            }
        return instance;
    }

    // открыть подключение к базе
    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        open = true;
    }

    // закрыть подключение к базе
    public void close() {
        if (mDBHelper != null) mDBHelper.close();
        open = false;
    }

    public boolean isOpen() {
        return open;
    }

    // получить все записи из DB_TABLE_NAME
    public Cursor getAllData() {
        return mDB.query(DB_TABLE_NAME, null, null, null, null, null, null);
    }

    // добавить запись в DB_TABLE_NAME
    public void addRec(long datatime, String todoname, String todoentry) {

        ContentValues cv = new ContentValues();

        cv.put(SQLiteHelper.COLUMN_DATA_TIME, datatime);
        cv.put(SQLiteHelper.COLUMN_TODO_NAME, todoname);
        cv.put(SQLiteHelper.COLUMN_TODO_ENTRY, todoentry);

        long rowID = mDB.insert(SQLiteHelper.DB_TABLE_NAME, null, cv);
        Log.d(LOG_TAG, "row inserted, COLUMN_ID = " + rowID);

    }

    // удалить запись из DB_TABLE_NAME
    public void delRec(long id) {
        mDB.delete(DB_TABLE_NAME, COLUMN_ID + " = " + id, null);
    }

    // получить запись из DB_TABLE_NAME
    public Cursor getRec(String id)
    {
        return mDB.rawQuery("Select*from " + DB_TABLE_NAME + " where id=" + id, null);
    }


    /**
     * DBHelper
    */
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("", "--- onCreate database ---");

            db.execSQL("create table " + DB_TABLE_NAME + " ("
                    + COLUMN_ID + " integer primary key autoincrement,"
                    + COLUMN_DATA_TIME  + " integer,"
                    + COLUMN_TODO_NAME  + " text,"
                    + COLUMN_TODO_ENTRY + " text"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    }

}
