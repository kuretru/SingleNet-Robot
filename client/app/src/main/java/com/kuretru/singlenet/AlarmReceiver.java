package com.kuretru.singlenet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String RECEIVER_NAME = "com.kuretru.singlenet.alarmReceiver";
    private SinglenetHandler _singlenetHandler;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(RECEIVER_NAME)) {
            initHandler(context);
            LogHelper.LogD("收到闹钟广播");
            LogHelper.LogFile(context, "开始执行任务");
            AlarmHelper alarmHelper = new AlarmHelper(context);
            alarmHelper.setNextAlarm();
            SmsHelper smsHelper = new SmsHelper(context, _singlenetHandler.handler);
            smsHelper.sendSxSms();
            DoAsync(60);
        }
    }

    private void initHandler(Context context) {
        SharedPreferences settings = context.getSharedPreferences(LogHelper.PREFS_NAME, 0);
        String url = settings.getString("url", "http://dorm.i5zhen.com:8079/sx");
        String secret = settings.getString("secret", "123456");
        _singlenetHandler = new SinglenetHandler(context, null);
        _singlenetHandler.setParameters(url, secret);
    }

    //设置一个超时时间(秒)，并等待任务的完成
    private void DoAsync(final int second) {
        Thread t = new Thread() {
            public void run() {
                for (int i = 0; i < second; i++) {
                    if (_singlenetHandler.finished) {
                        LogHelper.LogD("任务成功完成");
                        break;
                    } else if (i == second - 1) {
                        LogHelper.LogD("任务失败，超时");
                    }
                    try {
                        sleep(second * 100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }
}
