package com.kuretru.android.singlenet.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.kuretru.android.singlenet.util.ToastUtils;
import com.kuretru.android.singlenet.worker.SinglenetWorker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsReceiver extends BroadcastReceiver {

    public static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    private static final String TAG = "KT_SmsReceiver";
    private static final String SINGLENET_MOBILE = "106593005";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!ACTION.equals(intent.getAction())) {
            return;
        }
        SmsMessage[] messages = getMessagesFromIntent(intent);
        for (SmsMessage message : messages) {
            if (!SINGLENET_MOBILE.equals(message.getDisplayOriginatingAddress())) {
                continue;
            }
            String body = message.getDisplayMessageBody();
            Pattern pattern = Pattern.compile("[0-9]{6}");
            Matcher matcher = pattern.matcher(body);
            if (matcher.find()) {
                String code = matcher.group(0);
                doWork(context, code);
                break;
            }
        }
    }

    private void doWork(Context context, String code) {
        Log.i(TAG, "接收到闪讯密码短信：" + code);
        ToastUtils.show(context, "获取到闪讯密码：" + code);
        context.unregisterReceiver(this);

        WorkRequest workRequest = new OneTimeWorkRequest.Builder(SinglenetWorker.class)
                .setInputData(
                        new Data.Builder()
                                .putString("code", code)
                                .build()
                ).build();
        WorkManager.getInstance(context).enqueue(workRequest);
    }

    private SmsMessage[] getMessagesFromIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        Object[] pdus = (Object[]) bundle.get("pdus");
        SmsMessage[] result = new SmsMessage[pdus.length];
        for (int i = 0; i < pdus.length; i++) {
            byte[] bytes = (byte[]) pdus[i];
            result[i] = SmsMessage.createFromPdu(bytes);
        }
        return result;
    }

}
