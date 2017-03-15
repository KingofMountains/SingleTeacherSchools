package com.sts.singleteacherschool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sts.singleteacherschool.Data.Advisor;
import com.sts.singleteacherschool.Data.DatabaseHelper;
import com.sts.singleteacherschool.Utilities.Preferences;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    ProgressDialog loading;
    EditText txtUserId, txtPassword;
    ArrayList<Advisor> advisorList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loading = ProgressDialog.show(LoginActivity.this, "", "Loading..", false);

        txtUserId = (EditText) findViewById(R.id.txtUserID);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        new GetAdvisorsTask().execute("");
    }

    public void onLogin(View view) {

        Advisor data = userIdExists();

        if (null == data) {
            Toast.makeText(this, "User doesn't exists!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkPasswordMatch(data)) {
            Preferences.setAdvisorName(this,data.advisor_name);
            Preferences.setAdvisorSanchayatID(this,data.sanch_id);
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(0, R.anim.slide_out_left);
            finish();
        } else {
            Toast.makeText(this, "Incorrect Password!", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean checkPasswordMatch(Advisor advisor) {

        if (txtPassword.getText().toString().trim().equalsIgnoreCase(advisor.password))
            return true;
        else
            return false;
    }

    private Advisor userIdExists() {
        for (Advisor advisor : advisorList) {
            if (advisor.user_id.equalsIgnoreCase(txtUserId.getText().toString().trim())) {
                return advisor;
            }
        }
        return null;
    }

    class GetAdvisorsTask extends AsyncTask<String, String, Integer> {

        DatabaseHelper dbHelper;
        SQLiteDatabase db;

        @Override
        protected Integer doInBackground(String... params) {


            dbHelper = new DatabaseHelper(LoginActivity.this);
            db = dbHelper.getWritableDatabase();

            Cursor cursor = db.query("advisor", null, "live_id = ?", new String[]{"1"}, null, null, null);

            while (cursor.moveToNext()) {
                Advisor data = new Advisor();
                data.user_id = cursor.getString(cursor.getColumnIndex("user_id"));
                data.password = cursor.getString(cursor.getColumnIndex("password"));
                data.advisor_name = cursor.getString(cursor.getColumnIndex("name"));
                data.sanch_id = cursor.getString(cursor.getColumnIndex("sanch_id"));
                data.designation = cursor.getString(cursor.getColumnIndex("designation"));
                data.session_id = cursor.getString(cursor.getColumnIndex("session_id"));
                data.session_date = cursor.getString(cursor.getColumnIndex("session_date"));
                advisorList.add(data);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (loading != null && loading.isShowing()) {
                loading.dismiss();
            }

            dbHelper.close();

        }
    }
}
