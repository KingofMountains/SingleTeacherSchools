package com.sts.singleteacherschool;

import android.app.Application;
import android.content.Context;

public class STSApplication extends Application {

    public static Context parentActivity;
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.print("Application oncreate --------------");
        parentActivity = this;
    }
}
