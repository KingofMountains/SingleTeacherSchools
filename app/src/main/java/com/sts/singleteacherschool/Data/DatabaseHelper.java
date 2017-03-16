package com.sts.singleteacherschool.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "sts.sqlite";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
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
        db.execSQL("CREATE TABLE advisor_report (" +
                " id int(5) NOT NULL," +
                "  date varchar(100) NOT NULL," +
                "  advisor_username varchar(100) NOT NULL," +
                "  sanch varchar(50) NOT NULL," +
                "  village varchar(50) NOT NULL," +
                "  acharya varchar(50) NOT NULL," +
                "  st_strength_boys varchar(5) NOT NULL," +
                "  st_strength_girls varchar(5) NOT NULL," +
                "  st_strength_total varchar(5) NOT NULL," +
                "  attendance_boys varchar(5) NOT NULL," +
                "  attendance_girls varchar(5) NOT NULL," +
                "  attendance_total varchar(5) NOT NULL," +
                "  acharya_uniform varchar(50) NOT NULL," +
                "  black_board varchar(50) NOT NULL," +
                "  corp_name_board varchar(50) NOT NULL," +
                "  mats varchar(50) NOT NULL," +
                "  solar_lamp varchar(50) NOT NULL," +
                "  charts varchar(50) NOT NULL," +
                "  syllabus varchar(50) NOT NULL," +
                "  library_books varchar(50) NOT NULL," +
                "  medicine varchar(50) NOT NULL," +
                "  remarks varchar(300) NOT NULL," +
                "  advisor_last_visit varchar(50) NOT NULL," +
                "  last_visit_advisor_name varchar(100) NOT NULL," +
                "  img_1 varchar(255) NOT NULL," +
                "  img_2 varchar(255) NOT NULL," +
                "  img_3 varchar(255) NOT NULL," +
                "  img_4 varchar(255) NOT NULL," +
                "  logout_details datetime NOT NULL, PRIMARY KEY (id))");

        //creating sync table
        db.execSQL("CREATE TABLE sync_report (" +
                " id int(5) NOT NULL," +
                "  date varchar(100) NOT NULL," +
                "  advisor_username varchar(100) NOT NULL," +
                "  sanch varchar(50) NOT NULL," +
                "  village varchar(50) NOT NULL," +
                "  acharya varchar(50) NOT NULL," +
                "  st_strength_boys varchar(5) NOT NULL," +
                "  st_strength_girls varchar(5) NOT NULL," +
                "  st_strength_total varchar(5) NOT NULL," +
                "  attendance_boys varchar(5) NOT NULL," +
                "  attendance_girls varchar(5) NOT NULL," +
                "  attendance_total varchar(5) NOT NULL," +
                "  acharya_uniform varchar(50) NOT NULL," +
                "  black_board varchar(50) NOT NULL," +
                "  corp_name_board varchar(50) NOT NULL," +
                "  mats varchar(50) NOT NULL," +
                "  solar_lamp varchar(50) NOT NULL," +
                "  charts varchar(50) NOT NULL," +
                "  syllabus varchar(50) NOT NULL," +
                "  library_books varchar(50) NOT NULL," +
                "  medicine varchar(50) NOT NULL," +
                "  remarks varchar(300) NOT NULL," +
                "  advisor_last_visit varchar(50) NOT NULL," +
                "  last_visit_advisor_name varchar(100) NOT NULL," +
                "  img_1 varchar(255) NOT NULL," +
                "  img_2 varchar(255) NOT NULL," +
                "  img_3 varchar(255) NOT NULL," +
                "  img_4 varchar(255) NOT NULL," +
                "  logout_details datetime NOT NULL, PRIMARY KEY (id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertAdvisorReport(Report data, SQLiteDatabase db) {

        db.beginTransaction();
        try {

               ContentValues values = new ContentValues();
                values.clear();

                values.put("id", data.id);
                values.put("date", data.loggedInTime);
                values.put("advisor_username", data.advisorName);
                values.put("sanch", data.sanchayatName);
                values.put("village", data.villageName);
                values.put("acharya", data.acharyaName);
                values.put("st_strength_boys", data.boysActualStrength);
                values.put("st_strength_girls", data.girlsActualStrength);
                values.put("st_strength_total", data.totalActualStrength);
                values.put("attendance_boys", data.boysAttendanceStrength);
                values.put("attendance_girls", data.girlsAttendanceStrength);
                values.put("attendance_total", data.totalAttendanceStrength);
                values.put("acharya_uniform", data.uniform);
                values.put("black_board", data.blackboard);
                values.put("corp_name_board", data.corporate);
                values.put("mats", data.mats);
                values.put("solar_lamp", data.solarlamp);
                values.put("charts", data.charts);
                values.put("syllabus", data.syllabus);
                values.put("library_books", data.library);
                values.put("medicine", data.medicine);
                values.put("remarks", data.description);
                values.put("advisor_last_visit", data.advisorLastVisitDate);
                values.put("last_visit_advisor_name", data.lastVisitAdvisorName);
                values.put("img_1", data.imageone);
                values.put("img_2", data.imagetwo);
                values.put("img_3", data.imagethree);
                values.put("img_4", data.imagefour);
                values.put("logout_details", data.loggedOutTime);

                db.insertWithOnConflict("sync_report", null, values, SQLiteDatabase.CONFLICT_REPLACE);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return true;
    }

    public boolean insertAdvisorReport(ArrayList<Report> list, SQLiteDatabase db) {

        db.beginTransaction();
        try {
            for (Report data : list) {
                ContentValues values = new ContentValues();
                values.clear();

                values.put("id", data.id);
                values.put("date", data.loggedInTime);
                values.put("advisor_username", data.advisorName);
                values.put("sanch", data.sanchayatName);
                values.put("village", data.villageName);
                values.put("acharya", data.acharyaName);
                values.put("st_strength_boys", data.boysActualStrength);
                values.put("st_strength_girls", data.girlsActualStrength);
                values.put("st_strength_total", data.totalActualStrength);
                values.put("attendance_boys", data.boysAttendanceStrength);
                values.put("attendance_girls", data.girlsAttendanceStrength);
                values.put("attendance_total", data.totalAttendanceStrength);
                values.put("acharya_uniform", data.uniform);
                values.put("black_board", data.blackboard);
                values.put("corp_name_board", data.corporate);
                values.put("mats", data.mats);
                values.put("solar_lamp", data.solarlamp);
                values.put("charts", data.charts);
                values.put("syllabus", data.syllabus);
                values.put("library_books", data.library);
                values.put("medicine", data.medicine);
                values.put("remarks", data.description);
                values.put("advisor_last_visit", data.advisorLastVisitDate);
                values.put("last_visit_advisor_name", data.lastVisitAdvisorName);
                values.put("img_1", data.imageone);
                values.put("img_2", data.imagetwo);
                values.put("img_3", data.imagethree);
                values.put("img_4", data.imagefour);
                values.put("logout_details", data.loggedOutTime);

                db.insertWithOnConflict("advisor_report", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return true;
    }
}
