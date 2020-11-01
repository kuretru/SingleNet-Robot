package com.kuretru.android.singlenet.service;

import android.app.IntentService;
import android.content.Intent;
import android.telephony.SmsManager;

@Deprecated
public class SmsService extends IntentService {

    private static final String SINGLENET_MOBILE = "1065930051";
    private static final String SINGLENET_MESSAGE = "MM";

    private SmsManager smsManager;

    public SmsService() {
        super("SmsService");
        smsManager = SmsManager.getDefault();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        smsManager.sendTextMessage(SINGLENET_MOBILE, null, SINGLENET_MESSAGE, null, null);
    }

}
