package com.kuretru.android.singlenet.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.kuretru.android.singlenet.receiver.AlarmReceiver;

import java.util.Calendar;

import static android.app.AlarmManager.RTC_WAKEUP;
import static android.content.Context.ALARM_SERVICE;

public class AlarmService {

    private static final int SINGLENET_ALARM = 0;

    private Context context;
    private AlarmManager alarmManager;

    public AlarmService(Context context) {
        this.context = context;
        alarmManager = (android.app.AlarmManager) context.getSystemService(ALARM_SERVICE);
    }

    public void register() {
        long nextTime = getNextTime();
        PendingIntent pendingIntent = getPendingIntent();
        alarmManager.setExact(RTC_WAKEUP, nextTime, pendingIntent);
    }

    public void cancel() {
        PendingIntent pendingIntent = getPendingIntent();
        alarmManager.cancel(pendingIntent);
    }

    public boolean isRegistered() {
        PendingIntent pendingIntent = getPendingIntent(PendingIntent.FLAG_NO_CREATE);
        return pendingIntent != null;
    }

    public long getNextTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        if ((hour == 5 && minute > 50) || (hour > 5 && hour < 22) || (hour == 22 && minute < 50)) {
            calendar.set(Calendar.HOUR_OF_DAY, 22);
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, 5);
        }
        calendar.set(Calendar.MINUTE, 50);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime().getTime();
    }

    private PendingIntent getPendingIntent() {
        return getPendingIntent(PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getPendingIntent(int flag) {
        Intent intent = new Intent(AlarmReceiver.ACTION);
        intent.setComponent(new ComponentName(context, AlarmReceiver.class));
        return PendingIntent.getBroadcast(context, SINGLENET_ALARM, intent, flag);
    }

}
