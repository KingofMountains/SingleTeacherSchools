package com.sts.singleteacherschool.Utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

public class Utils {

    public static AlertDialog.Builder alert;

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
        }

        alert.setMessage(message).show();
    }
}
