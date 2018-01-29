package com.kuretru.singlenet;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogHelper {

    private static final String DEBUG_TAG = "KT-DEBUG";

    //返回当前时间
    public static String getTimeString(){
        return getTimeString(System.currentTimeMillis());
    }

    //返回指定时间戳的时间
    public static String getTimeString(Long time){
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Date now = new Date(time);
        String datetime = formatter.format(now);
        return datetime;
    }

    public static void LogD(String message){
        Log.d(DEBUG_TAG, String.format("%1$s %2$s", getTimeString(), message));
    }
}
