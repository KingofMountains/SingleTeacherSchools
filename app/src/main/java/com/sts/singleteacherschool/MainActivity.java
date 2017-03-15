package com.sts.singleteacherschool;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.sts.singleteacherschool.Data.Report;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    Activity thisActivity;
    public static Report data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        thisActivity = this;
        data = new Report();
        loadFormFragment();
    }

    @Override
    public void onFragmentInteraction(String from) {

        switch (from){
            case "continue":
                loadCaptureFragment();
                break;
            case "submit":
                submitReport();
                break;
        }
    }

    private void submitReport() {
        Toast.makeText(thisActivity, "Report Submitted Successfully!", Toast.LENGTH_SHORT).show();
    }

    private void loadCaptureFragment() {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, 0).replace(R.id.frmContainer, CaptureFragment.newInstance(), "capture").commit();
    }

    private void loadFormFragment() {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, 0).replace(R.id.frmContainer, FormFragment.newInstance(), "entry").commit();
    }


}
