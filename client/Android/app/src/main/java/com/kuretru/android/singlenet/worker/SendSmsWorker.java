package com.kuretru.android.singlenet.worker;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SendSmsWorker extends Worker {

    private static final String TAG = "SendSmsWorker";
    private static final String SINGLENET_MOBILE = "1065930051";
    private static final String SINGLENET_MESSAGE = "MM";

    public SendSmsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(TAG, "开始发送获取闪讯密码短信");
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(SINGLENET_MOBILE, null, SINGLENET_MESSAGE, null, null);
        Log.i(TAG, "成功发送获取闪讯密码短信");
        return Result.success();
    }

}
