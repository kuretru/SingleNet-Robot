package com.kuretru.singlenet;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogHelper {

    public static final String DEBUG_TAG = "KT-Singlenet-DEBUG";
    public static final String PREFS_NAME = "config";

    //返回当前时间
    public static String getTimeString() {
        return getTimeString(System.currentTimeMillis());
    }

    //返回指定时间戳的时间
    public static String getTimeString(Long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yy.MM.dd HH:mm:ss");
        Date now = new Date(time);
        String datetime = formatter.format(now);
        return datetime;
    }

    //将日志记录到Logcat
    public static void LogD(String message) {
        Log.d(DEBUG_TAG, String.format("%1$s %2$s", getTimeString(), message));
    }

    //将日志记录到配置文件
    public static void LogFile(Context context, String message) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        String old = settings.getString("log", "");
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("log", old + String.format("%1$s %2$s\n", getTimeString(), message));
        editor.commit();
    }
}
