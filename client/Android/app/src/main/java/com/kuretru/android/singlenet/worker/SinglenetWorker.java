package com.kuretru.android.singlenet.worker;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

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

import org.litepal.LitePal;

public class SinglenetWorker extends Worker {

    private static final String TAG = "KT_SinglenetWorker";

    private final Context context;
    private final String code;

    public SinglenetWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        this.code = getInputData().getString("code");
        LitePal.initialize(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "开始更新密码：" + code);
        NetworkOption networkOption = new NetworkOption(null, code);

        ServerConfig serverConfig = ConfigUtils.loadServerConfig(context);
        try {
            SinglenetApiService apiService = SinglenetApiServiceFactory.build(serverConfig);
            NetworkOption old = apiService.getNetworkOption();
            if (networkOption.getPassword().equals(old.getPassword())) {
                toastShow("密码相同未更新！");
                return Result.success();
            }
            apiService.setNetworkOption(networkOption);
            toastShow("更新密码成功！");
            InterfaceStatusEnum status = apiService.getInterfaceStatus();
            if (status == InterfaceStatusEnum.DOWN) {
                apiService.setInterfaceUp();
            }
        } catch (ApiServiceException e) {
            toastShow(e.getMessage());
            return Result.failure();
        }
        return Result.success();
    }

    private void toastShow(String message) {
        new Handler(Looper.getMainLooper()).post(() -> {
            ToastUtils.show(context, message);
        });
        Log.d(TAG, message);
        SystemLog systemLog = new SystemLog();
        systemLog.setTime(StringUtils.timestampToString(System.currentTimeMillis()));
        systemLog.setMessage(message);
        systemLog.save();
    }

}
