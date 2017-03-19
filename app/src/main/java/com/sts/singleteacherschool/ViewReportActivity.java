package com.sts.singleteacherschool;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sts.singleteacherschool.Data.DatabaseHelper;
import com.sts.singleteacherschool.Data.Report;
import com.sts.singleteacherschool.Fragments.ViewReportFragment;
import com.sts.singleteacherschool.Utilities.Preferences;

import java.util.ArrayList;

public class ViewReportActivity extends AppCompatActivity  {

    ViewPager reportPager;
    ReportPagerAdapter adapter;
    ProgressDialog loading;
    private int no_of_reports = 0;
    ArrayList<Report> list = new ArrayList<>();
    TextView lblNoReportsFound, lblPrev, lblNext;
    LinearLayout lnrNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        lnrNavigation = (LinearLayout) findViewById(R.id.lnrNavigation);
        lblPrev = (TextView) findViewById(R.id.lblPrev);
        lblNext = (TextView) findViewById(R.id.lblNext);
        lblNoReportsFound = (TextView) findViewById(R.id.lblNoReportsFound);
        reportPager = (ViewPager) findViewById(R.id.pagerReport);
        adapter = new ReportPagerAdapter(getSupportFragmentManager());

        getLastThirtyReports();

        reportPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                Toast.makeText(ViewReportActivity.this, "Selected Item --- " + position, Toast.LENGTH_SHORT).show();

                if (position == list.size() - 1) {
                    lblNext.setClickable(false);
                    lblNext.setTextColor(getResources().getColor(R.color.secondary_text));
                } else {
                    lblNext.setClickable(true);
                    lblNext.setTextColor(getResources().getColor(R.color.primary_text));
                }

                if (position == 0) {
                    lblPrev.setClickable(false);
                    lblPrev.setTextColor(getResources().getColor(R.color.secondary_text));
                } else {
                    lblPrev.setClickable(true);
                    lblPrev.setTextColor(getResources().getColor(R.color.primary_text));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void getLastThirtyReports() {

        loading = ProgressDialog.show(this, "", "Loading...", false);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String userID = Preferences.getUserID(this);
        Cursor cursor = db.rawQuery("select * from advisor_report where advisor_userid = '" + userID + "' order by id desc limit " +
                "30", null);

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

            list.add(data);

        }

        db.close();
        databaseHelper.close();

        if (list.size() > 0) {
            no_of_reports = list.size();
            reportPager.setAdapter(adapter);
            reportPager.setOffscreenPageLimit(0);
            lblPrev.setTextColor(getResources().getColor(R.color.secondary_text));
        } else {
            lblNoReportsFound.setVisibility(View.VISIBLE);
            lnrNavigation.setVisibility(View.GONE);
        }

        if (loading != null && loading.isShowing()) {
            loading.dismiss();
        }
    }

    public void onPrevious(View view) {
        reportPager.setCurrentItem(reportPager.getCurrentItem() - 1);
    }

    public void onNext(View view) {
        reportPager.setCurrentItem(reportPager.getCurrentItem() + 1);
    }

    private class ReportPagerAdapter extends FragmentStatePagerAdapter {

        public ReportPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ViewReportFragment.newInstance(list.get(position));
        }

        @Override
        public int getCount() {
            return no_of_reports;
        }
    }
}
