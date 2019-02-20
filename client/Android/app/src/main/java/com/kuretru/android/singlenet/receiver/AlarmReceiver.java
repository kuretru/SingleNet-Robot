package com.kuretru.android.singlenet.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String ACTION = "com.kuretru.android.singlenet.receiver.AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("onAlarmReceive");
    }

}
