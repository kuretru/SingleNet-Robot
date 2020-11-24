package com.kuretru.android.singlenet.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.kuretru.android.singlenet.api.service.SinglenetApiService;
import com.kuretru.android.singlenet.entity.InterfaceStatusEnum;
import com.kuretru.android.singlenet.entity.NetworkOption;
import com.kuretru.android.singlenet.entity.ServerConfig;
import com.kuretru.android.singlenet.entity.SystemLog;
import com.kuretru.android.singlenet.exception.ApiServiceException;
import com.kuretru.android.singlenet.factory.SinglenetApiServiceFactory;
import com.kuretru.android.singlenet.util.ConfigUtils;
import com.kuretru.android.singlenet.util.StringUtils;
import com.kuretru.android.singlenet.util.ToastUtils;

public class SinglenetService extends IntentService {

    private static final String TAG = "KT_SinglenetService";

    public SinglenetService() {
        super("SinglenetService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String code = intent.getStringExtra("code");
        Log.d(TAG, "开始更新密码：" + code);
        NetworkOption networkOption = new NetworkOption(null, code);

        ServerConfig serverConfig = ConfigUtils.loadServerConfig(this);
        try {
            SinglenetApiService apiService = SinglenetApiServiceFactory.build(serverConfig);
            NetworkOption old = apiService.getNetworkOption();
            if (networkOption.getPassword().equals(old.getPassword())) {
                toastShow("密码相同未更新！");
                return;
            }
            apiService.setNetworkOption(networkOption);
            toastShow("更新密码成功！");
            InterfaceStatusEnum status = apiService.getInterfaceStatus();
            if (status == InterfaceStatusEnum.DOWN) {
                apiService.setInterfaceUp();
            }
        } catch (ApiServiceException e) {
            toastShow("更新密码失败：" + e.getMessage());
        }
    }

    private void toastShow(String message) {
        new Handler(Looper.getMainLooper()).post(() -> {
            ToastUtils.show(SinglenetService.this, message);
        });
        Log.d(TAG, message);
        SystemLog systemLog = new SystemLog();
        systemLog.setTime(StringUtils.timestampToString(System.currentTimeMillis()));
        systemLog.setMessage(message);
        systemLog.save();
    }

}
