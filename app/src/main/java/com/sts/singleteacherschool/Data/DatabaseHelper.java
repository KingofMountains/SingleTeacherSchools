package com.sts.singleteacherschool.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "sts.sqlite";

    public DatabaseHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //creating advisor table
        db.execSQL("CREATE TABLE advisor (user_id varchar(20) NOT NULL,password varchar(20) NOT NULL,name varchar(20) NOT NULL,designation varchar(100) NOT NULL,sanch_id varchar(11) NOT NULL,live_id varchar(11) NOT NULL,session_id varchar(100) NOT NULL,session_date datetime NOT NULL)");

        //village table
        db.execSQL("CREATE TABLE village (id varchar(11) NOT NULL,village_name varchar(100) NOT NULL,sanch_id varchar(11) NOT NULL,live_id varchar(11) NOT NULL)");

        //sanchayat table
        db.execSQL("CREATE TABLE sanch (id varchar(11) NOT NULL,sanch_name varchar(100) NOT NULL,live_id varchar(11) NOT NULL)");

        //acharya table
        db.execSQL("CREATE TABLE acharya (id varchar(11) NOT NULL,acharya_name varchar(100) NOT NULL,sanch_id varchar(11) NOT NULL,village_name varchar(100) NOT NULL,village_id varchar(11) NOT NULL,live_id varchar(11) NOT NULL)");

        //advisor report table
//        db.execSQL("");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
