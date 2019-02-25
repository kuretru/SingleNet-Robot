package com.kuretru.android.singlenet.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kuretru.android.singlenet.service.AlarmService;
import com.kuretru.android.singlenet.service.SmsService;
import com.kuretru.android.singlenet.util.StringUtils;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String ACTION = "com.kuretru.android.singlenet.receiver.AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(StringUtils.DEBUG_TAG, "AlarmReceiver: onAlarmReceive");

        Intent smsIntent = new Intent(context, SmsService.class);
        context.startService(smsIntent);

        AlarmService alarmService = new AlarmService(context);
        alarmService.register();
    }

}
