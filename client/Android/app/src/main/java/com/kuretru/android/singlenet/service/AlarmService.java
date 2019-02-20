package com.kuretru.android.singlenet.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.kuretru.android.singlenet.util.AlarmUtils;

public class AlarmService extends IntentService {

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("xxxyyyzzz", "onAlarmService");
        AlarmUtils alarmUtils = new AlarmUtils(this);
        alarmUtils.register();
    }

}
