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

    private String _password;
    TextView _editTextServer;
    TextView _editTextSecret;
    Button _buttonGo;
    private Handler _handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String code = (String) msg.obj;
            HttpHelper httpHelper;
            String url = _editTextServer.getText().toString();
            switch (msg.what){
                case 1:
                    _password = code;
                    ToastShow("Step1 获取到闪讯密码：" + code);
                    httpHelper = new HttpHelper(getApplicationContext(), _handler);
                    httpHelper.getRouterPassword(url);
                    break;
                case 2:
                    ToastShow("Step2 当前路由器密码：" + code);
                    if(!_password.equals(code)){
                        httpHelper = new HttpHelper(getApplicationContext(), _handler);
                        String sec = _editTextSecret.getText().toString();
                        httpHelper.setRouterPassword(url,_password,sec);
                    }
                    break;
                case 3:
                    ToastShow("Step3 成功设置路由器密码：" + code);
                    break;
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
        _editTextSecret = (TextView) findViewById(R.id.editTextSecret);
    }

    private void ToastShow(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void buttonGoOnClick(View view){
        SmsHelper smsHelper = new SmsHelper(this ,_handler);
        smsHelper.sendSxSms();
    }
}
