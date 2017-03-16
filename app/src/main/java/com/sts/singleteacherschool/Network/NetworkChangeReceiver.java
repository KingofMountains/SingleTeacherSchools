package com.sts.singleteacherschool.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sts.singleteacherschool.Utilities.Utils;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Utils.hasInternet(context)) {
            context.startService(new Intent(context, ReportSyncService.class));
        }
    }
}
