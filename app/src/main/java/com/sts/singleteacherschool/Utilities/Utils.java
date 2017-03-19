package com.sts.singleteacherschool.Utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sts.singleteacherschool.Data.DatabaseHelper;
import com.sts.singleteacherschool.Data.LocationReport;
import com.sts.singleteacherschool.Data.Report;
import com.sts.singleteacherschool.Listeners.UploadListener;
import com.sts.singleteacherschool.Network.VolleySingleton;
import com.sts.singleteacherschool.R;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Utils {

    public static AlertDialog.Builder alert;
    private static int uploadedCount = 0;

    public static boolean hasInternet(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return false;
        }
        if (info.isRoaming()) {
            return true;
        }

        return true;
    }

    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("yyyy/MM/dd hh:mm:ss aa", cal).toString();
        return date;
    }

    public static void clearPreferences(Context context) {
        Preferences.setAdvisorName(context, "");
        Preferences.setAdvisorSanchayatID(context, "");
    }

    public static void showAlert(Activity thisActivity, String message) {

        if (null == alert) {
            alert = new AlertDialog.Builder(thisActivity);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        alert.setMessage(message).show();
    }


    public static void submitReport(final Context context, final Report data, final UploadListener listener) {

        System.out.println("-------------------------- submit report");

        final Map<String, String> images = getImagesMap(data);

        if (images.size() > 0) {

            for (final Map.Entry<String, String> e1 : images.entrySet()) {

                new Thread(new Runnable() {
                    public void run() {
                        int status = uploadFile(context, e1.getValue());
                        if (status == 200) {
                            String fileName = (new File(e1.getValue()).getName());
                            e1.setValue(context.getString(R.string.upload_image_path) + fileName);
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

                            postAdvisorReport(context, data, listener);
                            uploadedCount = 0;
                        }
                    }
                }).start();
            }

        }


    }

    public static Map<String, String> getImagesMap(Report data) {

        final Map<String, String> map = new HashMap<>();

        if (!data.imageone.equalsIgnoreCase("")) {
            map.put("one", data.imageone);
        }
        if (!data.imagetwo.equalsIgnoreCase("")) {
            map.put("two", data.imagetwo);
        }
        if (!data.imagethree.equalsIgnoreCase("")) {
            map.put("three", data.imagethree);
        }
        if (!data.imagefour.equalsIgnoreCase("")) {
            map.put("four", data.imagefour);
        }
        return map;
    }

    public static int uploadFile(Context context, String sourceFileUri) {

        String fileName = sourceFileUri;
        int serverResponseCode = 0;

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

            Log.e("uploadFile", "Source File not exist :" + sourceFileUri.toString());

            return 0;

        } else {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(context.getResources().getString(R.string.upload_images));

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
                    Log.e("Upload file to server", "- success");
                    uploadedCount++;

                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return serverResponseCode;

        } // End else block
    }

    public static void postAdvisorReport(Context context, final Report data, final UploadListener listener) {

        RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();

        String url = context.getResources().getString(R.string.post_advisor_report);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Postign onResponse --------- " + data.acharyaName);
                        listener.onUploadCompleted();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Postign onErrorResponse --------- " + data.acharyaName);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("date", data.loggedInTime);
                params.put("advisor_username", data.advisorName);
                params.put("advisor_userid", data.advisor_userid);
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
                System.out.println("Postign2 --------- " + data.acharyaName);
                return params;
            }

        };

        queue.add(stringRequest);

    }

    public static boolean backupDB(Context context) {
        try {
            String currentDBPath = "/data/data/" + context.getPackageName() + "/databases/sts.sqlite";
            String backupDBPath = "sts.sqlite";
            File currentDB = new File(currentDBPath);
            File backupDB = new File(Environment.getExternalStorageDirectory(), backupDBPath);

            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void submitNewLocationReport(Context context, final LocationReport data, final UploadListener listener) {

       RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();
        String url = context.getResources().getString(R.string.report_new_location);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Postign onResponse NewLocationReport --------- ");
                        listener.onUploadCompleted();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Postign onErrorResponse NewLocationReport --------- ");
                        listener.onUploadFailed();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("report_date", data.report_date);
                params.put("advisor_name", data.advisor_name);
                params.put("location", data.location);
                params.put("contact_name", data.contact_name);
                params.put("contact_no", data.contact_no);
                params.put("school_name", data.school_name);
                params.put("comments", data.comments);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public static void saveReportToPostLater(Context context,Report data) {
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            dbHelper.insertSyncReport(data, db);
            db.close();
            dbHelper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveNewLocationReportToPostLater(Context context, LocationReport data) {
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            dbHelper.insertLocationSyncReport(data, db);
            db.close();
            dbHelper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
