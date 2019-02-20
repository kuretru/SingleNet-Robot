package com.kuretru.android.singlenet.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.kuretru.android.singlenet.service.AlarmService;

import java.util.Calendar;

import static android.app.AlarmManager.RTC_WAKEUP;
import static android.content.Context.ALARM_SERVICE;

public class AlarmUtils {

    private static final int SINGLENET_ALARM = 0;

    private Context context;
    private AlarmManager alarmManager;

    public AlarmUtils(Context context) {
        this.context = context;
        alarmManager = (android.app.AlarmManager) context.getSystemService(ALARM_SERVICE);
    }

    public void register() {
        long nextTime = getDebugTime();
        PendingIntent pendingIntent = getPendingIntent();
        alarmManager.setExact(RTC_WAKEUP, System.currentTimeMillis() + 3000, pendingIntent);
    }

    public void cancel() {
        PendingIntent pendingIntent = getPendingIntent();
        alarmManager.cancel(pendingIntent);
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(context, AlarmService.class);
        return PendingIntent.getService(context, SINGLENET_ALARM, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private long getDebugTime() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, 5);
        return c.getTime().getTime();
    }

}
