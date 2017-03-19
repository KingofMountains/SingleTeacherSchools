package com.sts.singleteacherschool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sts.singleteacherschool.Data.DatabaseHelper;
import com.sts.singleteacherschool.Data.Report;
import com.sts.singleteacherschool.Network.VolleySingleton;
import com.sts.singleteacherschool.Utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity{

    RequestQueue queue;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        queue = VolleySingleton.getInstance(this).getRequestQueue();

        if (Utils.hasInternet(this)) {
            getAdvisorReportData();
        }
    }

    private void getAdvisorReportData() {

        String url = getString(R.string.get_all_report);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        new GetAdvisorReportTask().execute(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        loading = ProgressDialog.show(this, "", "Loading..", false);

        queue.add(stringRequest);
    }

    class GetAdvisorReportTask extends AsyncTask<String, String, Integer> {

        DatabaseHelper dbHelper;
        SQLiteDatabase db;
        ArrayList<Report> list = new ArrayList<>();
        boolean inserted = false;

        @Override
        protected Integer doInBackground(String... params) {

            System.out.println("response ------- " + params[0]);

            dbHelper = new DatabaseHelper(HomeActivity.this);
            db = dbHelper.getWritableDatabase();

            db.execSQL("delete from advisor_report");

            try {
                JSONObject resp = new JSONObject(params[0]);

                if (resp.has("advisor_report")) {

                    JSONArray advisor_report = resp.getJSONArray("advisor_report");

                    for (int i = 0; i < advisor_report.length(); i++) {
                        Report data = new Report();
                        JSONObject obj = advisor_report.getJSONObject(i);

                        data.id = obj.getInt("id");
                        data.advisorName = obj.getString("advisor_username");
                        data.advisor_userid = obj.getString("advisor_userid");
                        data.loggedInTime = obj.getString("date");
                        data.sanchayatName = obj.getString("sanch");
                        data.villageName = obj.getString("village");
                        data.acharyaName = obj.getString("acharya");
                        data.boysActualStrength = obj.getString("st_strength_boys");
                        data.girlsActualStrength = obj.getString("st_strength_girls");
                        data.totalActualStrength = obj.getString("st_strength_total");
                        data.boysAttendanceStrength = obj.getString("attendance_boys");
                        data.girlsAttendanceStrength = obj.getString("attendance_girls");
                        data.totalAttendanceStrength = obj.getString("attendance_total");
                        data.uniform = obj.getString("acharya_uniform");
                        data.blackboard = obj.getString("black_board");
                        data.corporate = obj.getString("corp_name_board");
                        data.mats = obj.getString("mats");
                        data.solarlamp = obj.getString("solar_lamp");
                        data.charts = obj.getString("charts");
                        data.syllabus = obj.getString("syllabus");
                        data.library = obj.getString("library_books");
                        data.medicine = obj.getString("medicine");
                        data.description = obj.getString("remarks");
                        data.advisorLastVisitDate = obj.getString("advisor_last_visit");
                        data.lastVisitAdvisorName = obj.getString("last_visit_advisor_name");
                        data.imageone = obj.getString("img_1");
                        data.imagetwo = obj.getString("img_2");
                        data.imagethree = obj.getString("img_3");
                        data.imagefour = obj.getString("img_4");
                        data.loggedOutTime = obj.getString("logout_details");

                        list.add(data);

                    }

                    inserted = dbHelper.insertAdvisorReports(list, db);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (loading != null && loading.isShowing()) {
                loading.dismiss();
            }
            db.close();
            dbHelper.close();
        }
    }

    public void onNewLocation(View view) {
        startActivity(new Intent(HomeActivity.this,NewLocationActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void onCreateReport(View view) {
        startActivity(new Intent(HomeActivity.this,ReportFormActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    public void onViewReport(View view) {
        startActivity(new Intent(HomeActivity.this,ViewReportActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
