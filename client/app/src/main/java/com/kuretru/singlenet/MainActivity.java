package com.kuretru.singlenet;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView _editTextServer;
    Button _buttonGo;
    private Handler _handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 2) {
                String code = (String) msg.obj;
                ToastShow(code);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        _buttonGo = (Button) findViewById(R.id.buttonGo);
        _editTextServer = (TextView) findViewById(R.id.editTextServer);
    }

    private void ToastShow(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void buttonGoOnClick(View view){
        //SmsHelper smsHelper = new SmsHelper(this);
        //smsHelper.sendSxSms();
        String url = _editTextServer.getText().toString();
        HttpHelper httpHelper = new HttpHelper(this, _handler);
        httpHelper.getRouterPassword(url);
    }
}
