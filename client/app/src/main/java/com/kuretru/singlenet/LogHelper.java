package com.kuretru.singlenet;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kuretru on 2018/1/29.
 */

public class LogHelper {
    public static String getTimeNow(){
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Date now = new Date(System.currentTimeMillis());
        String datetime = formatter.format(now);
        return datetime;
    }
}
