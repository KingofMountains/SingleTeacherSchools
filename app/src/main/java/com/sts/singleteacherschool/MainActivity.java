package com.sts.singleteacherschool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sts.singleteacherschool.Data.DatabaseHelper;
import com.sts.singleteacherschool.Data.Report;
import com.sts.singleteacherschool.Fragments.CaptureFragment;
import com.sts.singleteacherschool.Fragments.FormFragment;
import com.sts.singleteacherschool.Listeners.OnFragmentInteractionListener;
import com.sts.singleteacherschool.Network.VolleySingleton;
import com.sts.singleteacherschool.Utilities.Preferences;
import com.sts.singleteacherschool.Utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    Activity thisActivity;
    public static Report data;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;

    String upLoadServerUri = null;
    String uploadFilePath = "";
    final String uploadFileName = "temp.jpg";

    RequestQueue queue;
    ProgressDialog loading;

    int uploadedCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        thisActivity = this;
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        uploadFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        upLoadServerUri = "http://webmyls.com/sts/uploader.php";
        data = new Report();
        data.advisorName = Preferences.getAdvisorName(this);
        loadFormFragment();

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

    @Override
    public void onFragmentInteraction(String from) {

        switch (from) {
            case "continue":
                loadCaptureFragment();
                break;
            case "submit":
                dialog = ProgressDialog.show(MainActivity.this, "", "Uploading file...", false);
                submitReport();
                break;
        }
    }

    private void submitReport() {
//        Toast.makeText(thisActivity, "Report Submitted Successfully!", Toast.LENGTH_SHORT).show();

        data.loggedOutTime = Utils.getDate(System.currentTimeMillis());

        final Map<String, String> images = new HashMap<>();
        if (!data.imageone.equalsIgnoreCase("")) {
            images.put("one", data.imageone);
        }
        if (!data.imagetwo.equalsIgnoreCase("")) {
            images.put("two", data.imagetwo);
        }
        if (!data.imagethree.equalsIgnoreCase("")) {
            images.put("three", data.imagethree);
        }
        if (!data.imagefour.equalsIgnoreCase("")) {
            images.put("four", data.imagefour);
        }

        if (images.size() == 4) {

            for (final Map.Entry<String, String> e1 : images.entrySet()) {

                new Thread(new Runnable() {
                    public void run() {

                        if (Utils.hasInternet(MainActivity.this)) {
                            int status = uploadFile(e1.getValue());
                            if (status == 200) {
                                String fileName = (new File(e1.getValue()).getName());
                                e1.setValue("http://webmyls.com/sts/uploads/" + fileName);
                            }

                            if (uploadedCount == images.size()) {

                                for (final Map.Entry<String, String> e1 : images.entrySet()) {
                                    switch (e1.getKey()) {
                                        case "one":
                                            data.imageone = e1.getValue();
                                            break;
                                        case "two":
                                            data.imagetwo = e1.getValue();
                                            break;
                                        case "three":
                                            data.imagethree = e1.getValue();
                                            break;
                                        case "four":
                                            data.imagefour = e1.getValue();
                                            break;
                                    }
                                }

                                if (Utils.hasInternet(MainActivity.this)) {
                                    postAdvisorReport();
                                }
                                uploadedCount = 0;
                            }
                        } else {


                            try {

                                DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this);
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                dbHelper.insertAdvisorReport(data, db);

                                db.close();
                                dbHelper.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    dialog.cancel();
                                    Toast.makeText(thisActivity, "No Internet Connection!. Report will be posted once connected to internet.", Toast.LENGTH_SHORT).show();

                                    loadLoginActivity();
                                }
                            });


                        }
                    }
                }).start();
            }

        } else {
            Utils.showAlert(thisActivity, "Please select all images");
            dialog.dismiss();
        }


    }

    private void postAdvisorReport() {

        String url = getString(R.string.post_advisor_report);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this, "Report posted successfully!", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        loadLoginActivity();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        loadLoginActivity();
                        Toast.makeText(MainActivity.this, "Error occured while posting, Please try again!", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("date", data.loggedInTime);
                params.put("advisor_username", data.advisorName);
                params.put("sanch", data.sanchayatName);
                params.put("village", data.villageName);
                params.put("acharya", data.acharyaName);
                params.put("st_strength_boys", data.boysActualStrength);
                params.put("st_strength_girls", data.girlsActualStrength);
                params.put("st_strength_total", data.totalActualStrength);
                params.put("attendance_boys", data.boysAttendanceStrength);
                params.put("attendance_girls", data.girlsAttendanceStrength);
                params.put("attendance_total", data.totalAttendanceStrength);
                params.put("acharya_uniform", data.uniform);
                params.put("black_board", data.blackboard);
                params.put("corp_name_board", data.corporate);
                params.put("mats", data.mats);
                params.put("solar_lamp", data.solarlamp);
                params.put("charts", data.charts);
                params.put("syllabus", data.syllabus);
                params.put("library_books", data.library);
                params.put("medicine", data.medicine);
                params.put("remarks", data.description);
                params.put("advisor_last_visit", data.advisorLastVisitDate);
                params.put("last_visit_advisor_name", data.lastVisitAdvisorName);
                params.put("img_1", data.imageone);
                params.put("img_2", data.imagetwo);
                params.put("img_3", data.imagethree);
                params.put("img_4", data.imagefour);
                params.put("logout_details", data.loggedOutTime);

                return params;
            }

        };

        queue.add(stringRequest);

    }

    private void loadLoginActivity() {
        Utils.clearPreferences(MainActivity.this);
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void loadCaptureFragment() {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, 0).replace(R.id.frmContainer, CaptureFragment.newInstance(), "capture").commit();
    }

    private void loadFormFragment() {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, 0).replace(R.id.frmContainer, FormFragment.newInstance(), "entry").commit();
    }

    public int uploadFile(String sourceFileUri) {


        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            dialog.dismiss();

            Log.e("uploadFile", "Source File not exist :"
                    + uploadFilePath + "" + uploadFileName);

            return 0;

        } else {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=uploaded_file;filename=" + fileName + "" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {

                    uploadedCount++;

                    runOnUiThread(new Runnable() {
                        public void run() {

                            Toast.makeText(MainActivity.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

//                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

//                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });

            }
//            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }

    class GetAdvisorReportTask extends AsyncTask<String, String, Integer> {

        DatabaseHelper dbHelper;
        SQLiteDatabase db;
        ArrayList<Report> list = new ArrayList<>();
        boolean inserted = false;

        @Override
        protected Integer doInBackground(String... params) {

            System.out.println("response ------- " + params[0]);

            dbHelper = new DatabaseHelper(MainActivity.this);
            db = dbHelper.getWritableDatabase();

//            db.execSQL("delete from advisor_report");

            try {
                JSONObject resp = new JSONObject(params[0]);

                if (resp.has("advisor_report")) {

                    JSONArray advisor_report = resp.getJSONArray("advisor_report");

                    for (int i = 0; i < advisor_report.length(); i++) {
                        Report data = new Report();
                        JSONObject obj = advisor_report.getJSONObject(i);

                        data.id = obj.getInt("id");
                        data.advisorName = obj.getString("advisor_username");
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

                    inserted = dbHelper.insertAdvisorReport(list, db);
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


}
