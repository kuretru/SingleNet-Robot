package com.kuretru.singlenet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class AlarmHelper {

    private Context _context;
    private AlarmManager _alarmManager;

    public AlarmHelper(Context context) {
        _context = context;
        _alarmManager = (AlarmManager) _context.getSystemService(ALARM_SERVICE);
    }

    //设置下一次任务
    public String setNextAlarm() {
        long nextTime = getDebugTime();
        Intent alarmIntent = new Intent(AlarmReceiver.RECEIVER_NAME);
        PendingIntent broadcast = PendingIntent.getBroadcast(_context, 0, alarmIntent, 0);
        _alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextTime, broadcast);
        String strNextTime = LogHelper.getTimeString(nextTime);
        LogHelper.LogD("设置下一次任务时间：" + strNextTime);
        return strNextTime;
    }

    //获取下一次任务的时间
    private static long getNextTime() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 2);
        c.add(Calendar.MINUTE, 20);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if (hour > 0 && hour < 6) {
            c.set(Calendar.HOUR_OF_DAY, 5);
        } else {
            c.set(Calendar.HOUR_OF_DAY, 22);
        }
        c.set(Calendar.MINUTE, 50);
        c.set(Calendar.SECOND, 0);
        long time = c.getTime().getTime();
        return time;
    }

    //获取下一次任务的时间
    private static long getDebugTime() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, 2);
        long time = c.getTime().getTime();
        return time;
    }
}
