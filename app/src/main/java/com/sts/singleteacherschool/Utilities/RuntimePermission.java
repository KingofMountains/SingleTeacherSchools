package com.sts.singleteacherschool.Utilities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

public class RuntimePermission {

    public static boolean hasPermission(Context context, String permission) {

        if (context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
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
