package com.sts.singleteacherschool.Network;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.sts.singleteacherschool.Data.DatabaseHelper;
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

        Cursor cursor = db.rawQuery("select * from sync_report", null);

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

            System.out.println("Postign --------- "+data.acharyaName);

            Utils.submitReport(getApplicationContext(),data, new UploadListener() {
                @Override
                public void onUploadCompleted() {
                    System.out.println("deleting from sync_Report --------- "+data.id);
                    db.execSQL("delete from sync_report where id = "+data.id+"");
                    closeDB();
                    Toast.makeText(ReportSyncService.this, "Report posted successfully!", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    private void closeDB() {
        db.close();
        databaseHelper.close();
    }

//    private void submitReport(final Report data) {
//
//        final Map<String, String> images = new HashMap<>();
//        if (!data.imageone.equalsIgnoreCase("")) {
//            images.put("one", data.imageone);
//        }
//        if (!data.imagetwo.equalsIgnoreCase("")) {
//            images.put("two", data.imagetwo);
//        }
//        if (!data.imagethree.equalsIgnoreCase("")) {
//            images.put("three", data.imagethree);
//        }
//        if (!data.imagefour.equalsIgnoreCase("")) {
//            images.put("four", data.imagefour);
//        }
//
//        if (images.size() > 0) {
//
//            for (final Map.Entry<String, String> e1 : images.entrySet()) {
//
//                new Thread(new Runnable() {
//                    public void run() {
//                        int status = uploadFile(e1.getValue());
//                        if (status == 200) {
//                            String fileName = (new File(e1.getValue()).getName());
//                            e1.setValue("http://webmyls.com/sts/uploads/" + fileName);
//                        }
//
//                        if (uploadedCount == images.size()) {
//
//                            for (final Map.Entry<String, String> e1 : images.entrySet()) {
//                                switch (e1.getKey()) {
//                                    case "one":
//                                        data.imageone = e1.getValue();
//                                        break;
//                                    case "two":
//                                        data.imagetwo = e1.getValue();
//                                        break;
//                                    case "three":
//                                        data.imagethree = e1.getValue();
//                                        break;
//                                    case "four":
//                                        data.imagefour = e1.getValue();
//                                        break;
//                                }
//                            }
//
//                            postAdvisorReport(data);
//                            uploadedCount = 0;
//                        }
//                    }
//                }).start();
//            }
//
//        }
//
//
//    }
//
//    private void postAdvisorReport(final Report data) {
//
//        String url = getString(R.string.post_advisor_report);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        System.out.println("Postign3 --------- "+data.acharyaName);
//                        db.execSQL("select from sync_report where id = "+data.id+"");
//                        closeDB();
//                        Toast.makeText(getApplicationContext(), "Report posted successfully!", Toast.LENGTH_LONG).show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        System.out.println("Postign4--------- "+data.acharyaName);
//                        Toast.makeText(getApplicationContext(), "Error occured while posting, Please try again!", Toast.LENGTH_LONG).show();
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("date", data.loggedInTime);
//                params.put("advisor_username", data.advisorName);
//                params.put("sanch", data.sanchayatName);
//                params.put("village", data.villageName);
//                params.put("acharya", data.acharyaName);
//                params.put("st_strength_boys", data.boysActualStrength);
//                params.put("st_strength_girls", data.girlsActualStrength);
//                params.put("st_strength_total", data.totalActualStrength);
//                params.put("attendance_boys", data.boysAttendanceStrength);
//                params.put("attendance_girls", data.girlsAttendanceStrength);
//                params.put("attendance_total", data.totalAttendanceStrength);
//                params.put("acharya_uniform", data.uniform);
//                params.put("black_board", data.blackboard);
//                params.put("corp_name_board", data.corporate);
//                params.put("mats", data.mats);
//                params.put("solar_lamp", data.solarlamp);
//                params.put("charts", data.charts);
//                params.put("syllabus", data.syllabus);
//                params.put("library_books", data.library);
//                params.put("medicine", data.medicine);
//                params.put("remarks", data.description);
//                params.put("advisor_last_visit", data.advisorLastVisitDate);
//                params.put("last_visit_advisor_name", data.lastVisitAdvisorName);
//                params.put("img_1", data.imageone);
//                params.put("img_2", data.imagetwo);
//                params.put("img_3", data.imagethree);
//                params.put("img_4", data.imagefour);
//                params.put("logout_details", data.loggedOutTime);
//                System.out.println("Postign2 --------- "+data.acharyaName);
//                return params;
//            }
//
//        };
//
//        queue.add(stringRequest);
//
//    }
//
//    public int uploadFile(String sourceFileUri) {
//
//
//        String fileName = sourceFileUri;
//
//        HttpURLConnection conn = null;
//        DataOutputStream dos = null;
//        String lineEnd = "\r\n";
//        String twoHyphens = "--";
//        String boundary = "*****";
//        int bytesRead, bytesAvailable, bufferSize;
//        byte[] buffer;
//        int maxBufferSize = 1 * 1024 * 1024;
//        File sourceFile = new File(sourceFileUri);
//
//        if (!sourceFile.isFile()) {
//
//            Log.e("uploadFile", "Source File not exist :" + sourceFileUri.toString());
//
//            return 0;
//
//        } else {
//            try {
//
//                // open a URL connection to the Servlet
//                FileInputStream fileInputStream = new FileInputStream(sourceFile);
//                URL url = new URL(upLoadServerUri);
//
//                // Open a HTTP  connection to  the URL
//                conn = (HttpURLConnection) url.openConnection();
//                conn.setDoInput(true); // Allow Inputs
//                conn.setDoOutput(true); // Allow Outputs
//                conn.setUseCaches(false); // Don't use a Cached Copy
//                conn.setRequestMethod("POST");
//                conn.setRequestProperty("Connection", "Keep-Alive");
//                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
//                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
//                conn.setRequestProperty("uploaded_file", fileName);
//
//                dos = new DataOutputStream(conn.getOutputStream());
//
//                dos.writeBytes(twoHyphens + boundary + lineEnd);
//                dos.writeBytes("Content-Disposition: form-data; name=uploaded_file;filename=" + fileName + "" + lineEnd);
//                dos.writeBytes(lineEnd);
//
//                // create a buffer of  maximum size
//                bytesAvailable = fileInputStream.available();
//
//                bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                buffer = new byte[bufferSize];
//
//                // read file and write it into form...
//                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//                while (bytesRead > 0) {
//
//                    dos.write(buffer, 0, bufferSize);
//                    bytesAvailable = fileInputStream.available();
//                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//                }
//
//                // send multipart form data necesssary after file data...
//                dos.writeBytes(lineEnd);
//                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//
//                // Responses from the server (code and message)
//                serverResponseCode = conn.getResponseCode();
//                String serverResponseMessage = conn.getResponseMessage();
//
//                Log.i("uploadFile", "HTTP Response is : "
//                        + serverResponseMessage + ": " + serverResponseCode);
//
//                if (serverResponseCode == 200) {
//
//                    uploadedCount++;
//
//                }
//
//                //close the streams //
//                fileInputStream.close();
//                dos.flush();
//                dos.close();
//
//            } catch (MalformedURLException ex) {
//
////                dialog.dismiss();
//                ex.printStackTrace();
//
//                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
//            } catch (Exception e) {
//
////                dialog.dismiss();
//                e.printStackTrace();
//
//
//            }
////            dialog.dismiss();
//            return serverResponseCode;
//
//        } // End else block
//    }


}
