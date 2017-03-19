package com.sts.singleteacherschool;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sts.singleteacherschool.Data.DatabaseHelper;
import com.sts.singleteacherschool.Network.VolleySingleton;
import com.sts.singleteacherschool.Utilities.RuntimePermission;
import com.sts.singleteacherschool.Utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import static com.sts.singleteacherschool.Utilities.RuntimePermission.isMarshmallowOrGreater;


public class SplashActivity extends AppCompatActivity {
    private static final String READ_WRITE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final int READ_WRITE_PERMISSION_CODE = 601;
    File image_directory;
    RequestQueue queue;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        image_directory = new File(Environment.getExternalStorageDirectory(), "/.STS");
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        loading = (ProgressBar) findViewById(R.id.progressBar);

        if (isMarshmallowOrGreater()) {
            if(RuntimePermission.hasPermission(this,READ_WRITE_PERMISSION)) {
                makeDirectory();
            } else {
                requestPermissions(new String[]{READ_WRITE_PERMISSION}, READ_WRITE_PERMISSION_CODE);
            }
        } else {
            makeDirectory();
        }


    }

    private void continueToLogin() {

//        if(Utils.backupDB(this)) {
//            System.out.println("db copied");
//        } else {
//            System.out.println("db copied");
//        }

        if (Utils.hasInternet(this)) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String url = getString(R.string.get_advisor_village_sanch_acharya);
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            new GetAllDetailsTask().execute(response);
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println("SplashActivity ==== error response");
                                    error.printStackTrace();
                                    Toast.makeText(SplashActivity.this, "Slow network connection!, Please try again later.", Toast
                                            .LENGTH_LONG).show();
                                }
                            });

                            loading.setVisibility(View.VISIBLE);

                            queue.add(stringRequest);

                        }
                    });

                }
            }).start();
        } else {
            loadLoginActivity();
        }
    }

    private void makeDirectory() {

        if (!image_directory.exists()) {
            image_directory.mkdirs();
        }

        File mFileTemp = new File(image_directory.getPath().toString() + "/" + "temp.jpg");

        try {
            mFileTemp.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        continueToLogin();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == READ_WRITE_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeDirectory();
            } else {
                if(!shouldShowRequestPermissionRationale(READ_WRITE_PERMISSION)) {
                    RuntimePermission.showAlertforBlockedPermission(SplashActivity.this,"Storage Permission Denied","It seems Storage Permission is blocked!. Please enable the storage settings in settings page.","Go To Settings");
                } else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    class GetAllDetailsTask extends AsyncTask<String, String, Integer> {

        DatabaseHelper dbHelper;
        SQLiteDatabase db;

        @Override
        protected Integer doInBackground(String... params) {

            System.out.println("response ------- " + params[0]);

            dbHelper = new DatabaseHelper(SplashActivity.this);
            db = dbHelper.getWritableDatabase();

            db.execSQL("delete from advisor");
            db.execSQL("delete from village");
            db.execSQL("delete from sanch");
            db.execSQL("delete from acharya");

            try {
                JSONObject resp = new JSONObject(params[0]);

                if (resp.has("advisor")) {

                    JSONArray advisors = resp.getJSONArray("advisor");

                    for (int i = 0; i < advisors.length(); i++) {

                        JSONObject obj = advisors.getJSONObject(i);
                        ContentValues values = new ContentValues();
                        values.clear();

                        values.put("user_id", obj.getString("user_id"));
                        values.put("password", obj.getString("password"));
                        values.put("name", obj.getString("name"));
                        values.put("designation", obj.getString("designation"));
                        values.put("sanch_id", obj.getString("sanch_id"));
                        values.put("live_id", obj.getString("live_id"));
                        values.put("session_id", obj.getString("session_id"));
                        values.put("session_date", obj.getString("session_date"));

                        db.insert("advisor", null, values);
                    }

                }

                if (resp.has("village")) {

                    JSONArray village = resp.getJSONArray("village");

                    for (int i = 0; i < village.length(); i++) {

                        JSONObject obj = village.getJSONObject(i);
                        ContentValues values = new ContentValues();
                        values.clear();

                        values.put("id", obj.getString("id"));
                        values.put("village_name", obj.getString("village_name"));
                        values.put("sanch_id", obj.getString("sanch_id"));
                        values.put("live_id", obj.getString("live_id"));

                        db.insert("village", null, values);
                    }

                }

                if (resp.has("sanch")) {

                    JSONArray sanch = resp.getJSONArray("sanch");

                    for (int i = 0; i < sanch.length(); i++) {

                        JSONObject obj = sanch.getJSONObject(i);
                        ContentValues values = new ContentValues();
                        values.clear();

                        values.put("id", obj.getString("id"));
                        values.put("sanch_name", obj.getString("sanch_name"));
                        values.put("live_id", obj.getString("live_id"));

                        db.insert("sanch", null, values);
                    }

                }

                if (resp.has("acharya")) {

                    JSONArray acharya = resp.getJSONArray("acharya");

                    for (int i = 0; i < acharya.length(); i++) {

                        JSONObject obj = acharya.getJSONObject(i);
                        ContentValues values = new ContentValues();
                        values.clear();

                        values.put("id", obj.getString("id"));
                        values.put("acharya_name", obj.getString("acharya_name"));
                        values.put("sanch_id", obj.getString("sanch_id"));
                        values.put("village_name", obj.getString("village_name"));
                        values.put("village_id", obj.getString("village_id"));
                        values.put("live_id", obj.getString("live_id"));

                        db.insert("acharya", null, values);
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            loading.setVisibility(View.GONE);
            db.close();
            dbHelper.close();
            loadLoginActivity();
        }
    }

    private void loadLoginActivity() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}
