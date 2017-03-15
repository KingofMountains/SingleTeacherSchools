package com.sts.singleteacherschool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLogin(View view) {
        startActivity(new Intent(this,MainActivity.class));
        overridePendingTransition(0, R.anim.slide_out_left);
        finish();
    }
}
