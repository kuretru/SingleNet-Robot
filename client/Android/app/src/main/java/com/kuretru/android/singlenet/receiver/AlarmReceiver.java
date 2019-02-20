package com.kuretru.android.singlenet.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kuretru.android.singlenet.service.AlarmService;
import com.kuretru.android.singlenet.service.SmsService;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String ACTION = "com.kuretru.android.singlenet.receiver.AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("xxxyyyzzz", "onAlarmService");

        Intent singlenetIntent = new Intent(context, SmsService.class);
        context.startService(singlenetIntent);

        AlarmService alarmService = new AlarmService(context);
        alarmService.register();
    }

}
