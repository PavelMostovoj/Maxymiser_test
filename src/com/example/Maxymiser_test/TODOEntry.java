package com.example.Maxymiser_test;

import java.util.Calendar;

/**
 * Created by root on 06.08.15.
 */
public class TODOEntry {

    private long id;
    private long datatime;
    private String todoName;
    private String todoEntry;

    public TODOEntry()
    {
        this.datatime = Calendar.getInstance().getTimeInMillis();
        this.todoName = "Default To Do Name";
        this.todoEntry = "Default To Do Entry";
    }

    public TODOEntry(long id, long datatime, String todoName, String todoEntry)
    {
        this.id = id;
        this.datatime = datatime;
        this.todoName = todoName;
        this.todoEntry = todoEntry;
    }

    public long getId() {
        return id;
    }

    public long getDatatime() {
        return datatime;
    }

    public String getTodoName() {
        return todoName;
    }

    public String getTodoEntry() {
        return todoEntry;
    }

}
