package com.sts.singleteacherschool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.sts.singleteacherschool.Data.DatabaseHelper;
import com.sts.singleteacherschool.Data.Report;
import com.sts.singleteacherschool.Fragments.CaptureFragment;
import com.sts.singleteacherschool.Fragments.FormFragment;
import com.sts.singleteacherschool.Listeners.OnFragmentInteractionListener;
import com.sts.singleteacherschool.Listeners.UploadListener;
import com.sts.singleteacherschool.Utilities.Preferences;
import com.sts.singleteacherschool.Utilities.Utils;

import java.io.File;
import java.util.Map;

public class ReportFormActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    Activity thisActivity;
    public static Report data;
    ProgressDialog dialog = null;

    String upLoadServerUri = null;
    String uploadFilePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        thisActivity = this;
        uploadFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        upLoadServerUri = "http://webmyls.com/sts/uploader.php";
        data = new Report();
        data.advisorName = Preferences.getAdvisorName(this);
        data.advisor_userid = Preferences.getUserID(this);
        loadFormFragment();

    }


    @Override
    public void onFragmentInteraction(String from) {

        switch (from) {
            case "continue":
                loadCaptureFragment();
                break;
            case "submit":
                dialog = ProgressDialog.show(ReportFormActivity.this, "", "Uploading file...", false);
                onSubmitClicked();
                break;
        }
    }

    private void onSubmitClicked() {

        data.loggedOutTime = Utils.getDate(System.currentTimeMillis());

        final Map<String, String> images = Utils.getImagesMap(data);

        if (images.size() == 4) {

            new Thread(new Runnable() {
                public void run() {

                    if (Utils.hasInternet(ReportFormActivity.this)) {
                        Utils.submitReport(ReportFormActivity.this, data, new UploadListener() {
                            @Override
                            public void onUploadCompleted() {
                                Toast.makeText(ReportFormActivity.this, "Report posted successfully!", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                loadLoginActivity();
                            }
                        });
                    } else {

                        try {
                            DatabaseHelper dbHelper = new DatabaseHelper(ReportFormActivity.this);
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            dbHelper.insertSyncReport(data, db);
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

        } else {
            Utils.showAlert(thisActivity, "Please select all images");
            dialog.dismiss();
        }


    }

    private void loadLoginActivity() {
        Utils.clearPreferences(ReportFormActivity.this);
        startActivity(new Intent(ReportFormActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void loadCaptureFragment() {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, 0).replace(R.id.frmContainer, CaptureFragment.newInstance(), "capture").commit();
    }

    private void loadFormFragment() {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, 0).replace(R.id.frmContainer, FormFragment.newInstance(), "entry").commit();
    }

}
