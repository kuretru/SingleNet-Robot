package com.kuretru.singlenet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button _buttonGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        _buttonGo=(Button) findViewById(R.id.buttonGo);
    }

    private void ToastShow(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void buttonGoOnClick(View view){
        SmsHelper smsHelper = new SmsHelper(this);
        smsHelper.sendSxSms();
    }
}
