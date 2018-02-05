package com.kuretru.singlenet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String RECEIVER_NAME = "com.kuretru.singlenet.alarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(RECEIVER_NAME)) {
            LogHelper.LogD("收到闹钟广播");
            LogHelper.LogFile(context, "收到闹钟广播");
            AlarmHelper alarmHelper = new AlarmHelper(context);
            alarmHelper.setNextAlarm();
        }
    }
}
