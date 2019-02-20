package com.kuretru.android.singlenet.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.kuretru.android.singlenet.service.SinglenetService;
import com.kuretru.android.singlenet.util.ToastUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsReceiver extends BroadcastReceiver {

    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private static final String SINGLENET_MOBILE = "106593005";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!ACTION.equals(action)) {
            return;
        }
        SmsMessage[] messages = getMessagesFromIntent(intent);
        if (messages != null && messages.length > 0) {
            for (SmsMessage message : messages) {
                if (SINGLENET_MOBILE.equals(message.getDisplayOriginatingAddress())) {
                    String body = message.getDisplayMessageBody();
                    Pattern pattern = Pattern.compile("[0-9]{6}");
                    Matcher matcher = pattern.matcher(body);
                    if (matcher.find()) {
                        String code = matcher.group(0);
                        Intent singlenetIntent = new Intent(context, SinglenetService.class);
                        singlenetIntent.putExtra("code", code);
                        context.startService(singlenetIntent);
                        ToastUtils.show(context, "获取到闪讯密码：" + code);
                        break;
                    }
                }
            }
        }
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
