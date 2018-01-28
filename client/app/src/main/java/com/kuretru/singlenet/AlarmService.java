package com.kuretru.singlenet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import java.util.Date;

public class AlarmService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("Singnet-DEBUG", "打印时间: " + new Date().toString());
            }
        }).start();
        setAlarm();
        return START_STICKY;
    }

    public void setAlarm(){
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int five = 5000; // 这是5s
        long triggerAtTime = SystemClock.elapsedRealtime() + five;

        Intent i = new Intent(this, AlarmReceiver.class);
        i.setAction("com.kuretru.singlenet");
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
    }
}
