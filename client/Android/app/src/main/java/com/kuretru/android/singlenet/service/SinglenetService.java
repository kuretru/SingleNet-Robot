package com.kuretru.android.singlenet.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

public class SinglenetService extends IntentService {

    public SinglenetService() {
        super("SinglenetService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        String code = bundle.getString("code");
    }

}
