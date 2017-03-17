package com.sts.singleteacherschool.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {


    public static void setAdvisorName(Context mActivity, String name) {
        SharedPreferences userPreference = PreferenceManager.getDefaultSharedPreferences(mActivity);
        SharedPreferences.Editor editor = userPreference.edit();
        editor.putString("advisor_name", name); // value to store
        editor.commit();
    }

    public static String getAdvisorName(Context mActivity) {
        SharedPreferences userPreference = PreferenceManager.getDefaultSharedPreferences(mActivity);
        return userPreference.getString("advisor_name", "");
    }

    public static void setAdvisorSanchayatID(Context mActivity, String sanch_id) {
        SharedPreferences userPreference = PreferenceManager.getDefaultSharedPreferences(mActivity);
        SharedPreferences.Editor editor = userPreference.edit();
        editor.putString("sanchayatid", sanch_id); // value to store
        editor.commit();
    }

    public static String geAdvisorSanchayatID(Context mActivity) {
        SharedPreferences userPreference = PreferenceManager.getDefaultSharedPreferences(mActivity);
        return userPreference.getString("sanchayatid", "");
    }


}
