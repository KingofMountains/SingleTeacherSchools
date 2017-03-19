package com.sts.singleteacherschool.Network;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.sts.singleteacherschool.Data.DatabaseHelper;
import com.sts.singleteacherschool.Data.LocationReport;
import com.sts.singleteacherschool.Data.Report;
import com.sts.singleteacherschool.Listeners.UploadListener;
import com.sts.singleteacherschool.R;
import com.sts.singleteacherschool.Utilities.Utils;

public class ReportSyncService extends IntentService {

    RequestQueue queue;
    int uploadedCount = 0;
    int serverResponseCode = 0;
    String upLoadServerUri = null;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    public ReportSyncService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();

        queue = VolleySingleton.getInstance(this).getRequestQueue();
        upLoadServerUri = getString(R.string.upload_images);

        final Cursor cursor = db.rawQuery("select * from sync_report", null);

        while (cursor.moveToNext()) {

            final Report data = new Report();

            data.id = cursor.getInt(cursor.getColumnIndex("id"));
            data.advisorName = cursor.getString(cursor.getColumnIndex("advisor_username"));
            data.advisor_userid = cursor.getString(cursor.getColumnIndex("advisor_userid"));
            data.loggedInTime = cursor.getString(cursor.getColumnIndex("date"));
            data.sanchayatName = cursor.getString(cursor.getColumnIndex("sanch"));
            data.villageName = cursor.getString(cursor.getColumnIndex("village"));
            data.acharyaName = cursor.getString(cursor.getColumnIndex("acharya"));
            data.boysActualStrength = cursor.getString(cursor.getColumnIndex("st_strength_boys"));
            data.girlsActualStrength = cursor.getString(cursor.getColumnIndex("st_strength_girls"));
            data.totalActualStrength = cursor.getString(cursor.getColumnIndex("st_strength_total"));
            data.boysAttendanceStrength = cursor.getString(cursor.getColumnIndex("attendance_boys"));
            data.girlsAttendanceStrength = cursor.getString(cursor.getColumnIndex("attendance_girls"));
            data.totalAttendanceStrength = cursor.getString(cursor.getColumnIndex("attendance_total"));
            data.uniform = cursor.getString(cursor.getColumnIndex("acharya_uniform"));
            data.blackboard = cursor.getString(cursor.getColumnIndex("black_board"));
            data.corporate = cursor.getString(cursor.getColumnIndex("corp_name_board"));
            data.mats = cursor.getString(cursor.getColumnIndex("mats"));
            data.solarlamp = cursor.getString(cursor.getColumnIndex("solar_lamp"));
            data.charts = cursor.getString(cursor.getColumnIndex("charts"));
            data.syllabus = cursor.getString(cursor.getColumnIndex("syllabus"));
            data.library = cursor.getString(cursor.getColumnIndex("library_books"));
            data.medicine = cursor.getString(cursor.getColumnIndex("medicine"));
            data.description = cursor.getString(cursor.getColumnIndex("remarks"));
            data.advisorLastVisitDate = cursor.getString(cursor.getColumnIndex("advisor_last_visit"));
            data.lastVisitAdvisorName = cursor.getString(cursor.getColumnIndex("last_visit_advisor_name"));
            data.imageone = cursor.getString(cursor.getColumnIndex("img_1"));
            data.imagetwo = cursor.getString(cursor.getColumnIndex("img_2"));
            data.imagethree = cursor.getString(cursor.getColumnIndex("img_3"));
            data.imagefour = cursor.getString(cursor.getColumnIndex("img_4"));
            data.loggedOutTime = cursor.getString(cursor.getColumnIndex("logout_details"));

            System.out.println("Postign --------- " + data.acharyaName);

            Utils.submitReport(getApplicationContext(), data, new UploadListener() {
                @Override
                public void onUploadCompleted() {
                    System.out.println("deleting from sync_Report --------- " + data.id);
                    db.execSQL("delete from sync_report where id = " + data.id + "");
                    cursor.close();
                    closeDB();
                    Toast.makeText(ReportSyncService.this, "Report posted successfully!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onUploadFailed() {

                }
            });
        }


        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();

        final Cursor c = db.rawQuery("select * from sync_location_report", null);

        while (c.moveToNext()) {

            final LocationReport data = new LocationReport();

            data.report_date = c.getString(c.getColumnIndex("report_date"));
            data.advisor_name = c.getString(c.getColumnIndex("advisor_name"));
            data.location = c.getString(c.getColumnIndex("location"));
            data.contact_name = c.getString(c.getColumnIndex("contact_name"));
            data.contact_no = c.getString(c.getColumnIndex("contact_no"));
            data.school_name = c.getString(c.getColumnIndex("school_name"));
            data.comments = c.getString(c.getColumnIndex("comments"));

            System.out.println("Postign new location --------- " + data.advisor_name);

            Utils.submitNewLocationReport(getApplicationContext(), data, new UploadListener() {
                @Override
                public void onUploadCompleted() {
                    System.out.println("deleting from new location sync_Report --------- " + data.advisor_name);
                    db.execSQL("delete from sync_location_report where report_date = '" + data.report_date + "'");
                    c.close();
                    closeDB();
                    Toast.makeText(ReportSyncService.this, "Report posted successfully!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onUploadFailed() {

                }
            });
        }

    }

    private void closeDB() {
        db.close();
        databaseHelper.close();
    }

}
