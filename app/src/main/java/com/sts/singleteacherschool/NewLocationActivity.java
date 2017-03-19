package com.sts.singleteacherschool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.sts.singleteacherschool.Data.LocationReport;
import com.sts.singleteacherschool.Listeners.UploadListener;
import com.sts.singleteacherschool.Network.VolleySingleton;
import com.sts.singleteacherschool.Utilities.Preferences;
import com.sts.singleteacherschool.Utilities.Utils;

public class NewLocationActivity extends AppCompatActivity {

    EditText txtLocation, txtSchool, txtContact, txtContactPhone, txtComments;
    Button btnSubmit;
    RequestQueue queue;
    ProgressDialog loading;
    Activity thisActivity;
    String reportDate = "";
    LocationReport data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_location);
        thisActivity = this;
        init();
    }

    private void init() {

        txtLocation = (EditText) findViewById(R.id.txtLocationName);
        txtSchool = (EditText) findViewById(R.id.txtSchoolName);
        txtContact = (EditText) findViewById(R.id.txtContactPerson);
        txtContactPhone = (EditText) findViewById(R.id.txtContactNumber);
        txtComments = (EditText) findViewById(R.id.txtComments);
        btnSubmit = (Button) findViewById(R.id.btnNewSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                queue = VolleySingleton.getInstance(thisActivity).getRequestQueue();

                data = new LocationReport();

                data.report_date = Utils.getDate(System.currentTimeMillis());
                data.advisor_name = Preferences.getAdvisorName(thisActivity);
                data.location = txtLocation.getText().toString().trim();
                data.contact_name = txtContact.getText().toString().trim();
                data.contact_no = txtContactPhone.getText().toString().trim();
                data.school_name = txtSchool.getText().toString().trim();
                data.comments = txtComments.getText().toString().trim();

                if (checkDetailsEntered(data)) {

                    loading = ProgressDialog.show(thisActivity,"","Loading...",false);

                    if (Utils.hasInternet(NewLocationActivity.this)) {

                        Utils.submitNewLocationReport(thisActivity, data, new UploadListener() {
                            @Override
                            public void onUploadCompleted() {
                                Toast.makeText(thisActivity, "New Location Report posted successfully!", Toast.LENGTH_SHORT).show();
                                finish();
                                loading.dismiss();
                            }

                            @Override
                            public void onUploadFailed() {
                                onNoInternetOrFailed("Failed to post due bad network.Report will be posted once connected to internet.");
                            }
                        });

                    } else {
                        onNoInternetOrFailed("No Internet Connection!. New Location Report will be posted once connected to internet" +
                                ".");
                     }
                }
            }
        });
    }

    private void onNoInternetOrFailed(String message) {
        Utils.saveNewLocationReportToPostLater(thisActivity,data);
        loading.dismiss();
        Toast.makeText(thisActivity, message, Toast.LENGTH_SHORT).show();
        finish();
    }

    private boolean checkDetailsEntered(LocationReport data) {
        if (data.location.equalsIgnoreCase("")) {
            Utils.showAlert(thisActivity, "Please enter Location Name");
            return false;
        } else if (data.school_name.equalsIgnoreCase("")) {
            Utils.showAlert(thisActivity, "Please enter School Name");
            return false;
        } else if (data.comments.equalsIgnoreCase("")) {
            Utils.showAlert(thisActivity, "Please enter comments");
            return false;
        }
        return true;
    }
}
