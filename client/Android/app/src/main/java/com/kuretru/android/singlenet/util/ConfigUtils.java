package com.kuretru.android.singlenet.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.kuretru.android.singlenet.constant.SystemConstants;
import com.kuretru.android.singlenet.entity.ServerConfig;

import static android.content.Context.MODE_PRIVATE;

public class ConfigUtils {

    private static final String SPLITTER = ",";

    /**
     * 从配置文件读取服务端配置
     *
     * @param context Context
     * @return 服务端配置
     */
    public static ServerConfig loadServerConfig(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", MODE_PRIVATE);
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setServerUrl(sharedPreferences.getString(SystemConstants.CONFIG_SERVER_URL, ""));
        serverConfig.setServerUrlHistory(StringUtils.stringToList(sharedPreferences.getString(SystemConstants.CONFIG_SERVER_URL_HISTORY, ""), SPLITTER));
        serverConfig.setVerifySsl(sharedPreferences.getString(SystemConstants.CONFIG_VERIFY_SSL, "verify"));
        serverConfig.setNetworkInterface(sharedPreferences.getString(SystemConstants.CONFIG_NETWORK_INTERFACE, "wan"));
        serverConfig.setServerType(sharedPreferences.getString(SystemConstants.CONFIG_SERVER_TYPE, SystemConstants.CONFIG_SERVER_TYPE_LUCI_RPC));
        serverConfig.setUsername(sharedPreferences.getString(SystemConstants.CONFIG_USERNAME, "root"));
        serverConfig.setPassword(sharedPreferences.getString(SystemConstants.CONFIG_PASSWORD, ""));
        serverConfig.setAuthToken(sharedPreferences.getString(SystemConstants.CONFIG_AUTH_TOKEN, ""));
        serverConfig.setSimCard(sharedPreferences.getString(SystemConstants.CONFIG_SIM_CARD, SystemConstants.CONFIG_SIM_CARD_DEFAULT));
        serverConfig.setInterval(sharedPreferences.getInt(SystemConstants.CONFIG_INTERVAL, SystemConstants.CONFIG_INTERVAL_DEFAULT));
        return serverConfig;
    }

    /**
     * 保存服务端配置至配置文件
     *
     * @param context      Context
     * @param serverConfig 服务端配置
     */
    public static void saveServerConfig(Context context, ServerConfig serverConfig) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SystemConstants.CONFIG_SERVER_URL, serverConfig.getServerUrl());
        editor.putString(SystemConstants.CONFIG_SERVER_URL_HISTORY, StringUtils.listToString(serverConfig.getServerUrlHistory(), SPLITTER));
        editor.putString(SystemConstants.CONFIG_VERIFY_SSL, serverConfig.getVerifySsl());
        editor.putString(SystemConstants.CONFIG_NETWORK_INTERFACE, serverConfig.getNetworkInterface());
        editor.putString(SystemConstants.CONFIG_SERVER_TYPE, serverConfig.getServerType());
        editor.putString(SystemConstants.CONFIG_USERNAME, serverConfig.getUsername());
        editor.putString(SystemConstants.CONFIG_PASSWORD, serverConfig.getPassword());
        editor.putString(SystemConstants.CONFIG_AUTH_TOKEN, serverConfig.getAuthToken());
        editor.putString(SystemConstants.CONFIG_SIM_CARD, serverConfig.getSimCard());
        editor.putInt(SystemConstants.CONFIG_INTERVAL, serverConfig.getInterval());
        editor.apply();
    }

    /**
     * 清空配置文件
     *
     * @param context Context
     */
    public static void clearServerConfig(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SystemConstants.CONFIG_SERVER_URL);
        editor.remove(SystemConstants.CONFIG_SERVER_URL_HISTORY);
        editor.remove(SystemConstants.CONFIG_VERIFY_SSL);
        editor.remove(SystemConstants.CONFIG_NETWORK_INTERFACE);
        editor.remove(SystemConstants.CONFIG_SERVER_TYPE);
        editor.remove(SystemConstants.CONFIG_USERNAME);
        editor.remove(SystemConstants.CONFIG_PASSWORD);
        editor.remove(SystemConstants.CONFIG_AUTH_TOKEN);
        editor.remove(SystemConstants.CONFIG_SIM_CARD);
        editor.remove(SystemConstants.CONFIG_INTERVAL);
        editor.apply();
    }

}
