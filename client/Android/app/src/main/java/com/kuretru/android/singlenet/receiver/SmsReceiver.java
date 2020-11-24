package com.kuretru.android.singlenet.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.kuretru.android.singlenet.service.SinglenetService;
import com.kuretru.android.singlenet.util.ToastUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsReceiver extends BroadcastReceiver {

    private static final String TAG = "KT_SmsReceiver";
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
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
        Intent intent = new Intent(context, SinglenetService.class);
        intent.putExtra("code", code);
        context.startService(intent);
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
