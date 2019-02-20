package com.kuretru.android.singlenet.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.kuretru.android.singlenet.util.ToastUtils;

public class SinglenetService extends IntentService {

    public SinglenetService() {
        super("SinglenetApi");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Bundle bundle = intent.getExtras();
        //String code = bundle.getString("code");
    }

}
