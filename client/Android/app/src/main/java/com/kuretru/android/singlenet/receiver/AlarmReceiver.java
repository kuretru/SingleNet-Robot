package com.kuretru.android.singlenet.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.kuretru.android.singlenet.service.AlarmService;
import com.kuretru.android.singlenet.worker.SendSmsWorker;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String ACTION = "com.kuretru.android.singlenet.receiver.AlarmReceiver";

    private static final String TAG = "KT_AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onAlarmReceive");

        WorkRequest workRequest = OneTimeWorkRequest.from(SendSmsWorker.class);
        WorkManager.getInstance(context).enqueue(workRequest);

        AlarmService alarmService = new AlarmService(context);
        alarmService.register();
    }

}
