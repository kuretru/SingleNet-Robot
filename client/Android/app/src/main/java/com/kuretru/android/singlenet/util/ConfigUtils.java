package com.kuretru.android.singlenet.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.kuretru.android.singlenet.entity.ServerConfig;

import static android.content.Context.MODE_PRIVATE;

public class ConfigUtils {

    public static ServerConfig loadServerConfig(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", MODE_PRIVATE);
        String url = sharedPreferences.getString("config_url", "");
        String secret = sharedPreferences.getString("config_secret", "");
        return new ServerConfig(url, secret);
    }

}
