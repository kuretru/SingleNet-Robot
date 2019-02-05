package com.kuretru.android.singlenet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kuretru.android.singlenet.R;
import com.kuretru.android.singlenet.service.SmsService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnConfig_onClick(View view) {
        Intent intent = new Intent(this, ConfigActivity.class);
        this.startActivity(intent);
    }

    public void btnSend_onClick(View view) {
        Intent intent = new Intent(this, SmsService.class);
        this.startService(intent);
    }

}
