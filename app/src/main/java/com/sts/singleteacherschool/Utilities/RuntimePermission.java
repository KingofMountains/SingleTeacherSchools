package com.sts.singleteacherschool.Utilities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;

public class RuntimePermission {

    public static boolean isMarshmallowOrGreater(){

        if(Build.VERSION.SDK_INT >= 23)
            return true;

        return false;
    }

    public static boolean hasPermission(Activity activity, String permission) {

        if (ContextCompat.checkSelfPermission(activity,permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }



    public static void showAlertforBlockedPermission(final Activity activity, String title, String message, String btnText) {

        new AlertDialog.Builder(activity).setTitle(title).setMessage(message).setPositiveButton(btnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
                activity.startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse
                        ("package:" + activity.getPackageName())));
            }
        }).setCancelable(false).create().show();
    }

}
