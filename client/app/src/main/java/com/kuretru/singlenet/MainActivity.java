package com.kuretru.singlenet;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "config";
    private String _password;
    TextView _editTextServer;
    TextView _editTextSecret;
    TextView _textViewTime;
    TextView _textViewLog;
    Button _buttonGo;
    Button _buttonSave;

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
                    toastShow("Step1 获取到闪讯密码：" + code);
                    httpHelper = new HttpHelper(getApplicationContext(), _handler);
                    httpHelper.getRouterPassword(url);
                    break;
                case 2:
                    toastShow("Step2 当前路由器密码：" + code);
                    if(!_password.equals(code)){
                        httpHelper = new HttpHelper(getApplicationContext(), _handler);
                        String sec = _editTextSecret.getText().toString();
                        httpHelper.setRouterPassword(url,_password,sec);
                    }else{
                        appendLog(String.format("%1$s 获取密码：%2$s，未更新\n",
                                LogHelper.getTimeString(), code));
                    }
                    break;
                case 3:
                    toastShow("Step3 成功设置路由器密码：" + code);
                    appendLog(String.format("%1$s 获取密码：%2$s，成功更新\n",
                            LogHelper.getTimeString(), code));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        loadConfig();
    }

    private void initView(){
        _editTextServer = (TextView) findViewById(R.id.editTextServer);
        _editTextSecret = (TextView) findViewById(R.id.editTextSecret);
        _textViewTime = (TextView) findViewById(R.id.textViewTime);
        _textViewLog = (TextView) findViewById(R.id.textViewLog);
        _buttonGo = (Button) findViewById(R.id.buttonGo);
        _buttonSave = (Button) findViewById(R.id.buttonSave);
        _textViewLog.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    private void loadConfig(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String url = settings.getString("url", "http://dorm.i5zhen.com:8079/sx");
        String pwd = settings.getString("secret","123456");
        _editTextServer.setText(url);
        _editTextSecret.setText(pwd);
    }

    private void toastShow(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void buttonGoOnClick(View view){
        SmsHelper smsHelper = new SmsHelper(this ,_handler);
        smsHelper.sendSxSms();
    }

    public void buttonSaveOnClick(View view){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("url",_editTextServer.getText().toString());
        editor.putString("secret",_editTextSecret.getText().toString());
        editor.commit();
        toastShow("配置保存成功");
    }

    public void buttonAlarmOnClick(View view){
        AlarmHelper alarmHelper = new AlarmHelper(this);
        LogHelper.LogD("注册定时任务成功");
        toastShow("注册定时任务成功");
        String time = alarmHelper.setNextAlarm();
        _textViewTime.setText("下一次任务时间：\n" + time);

    }

    private void appendLog(String text){
        _textViewLog.append(text);
        int offset = _textViewLog.getLineCount() * _textViewLog.getLineHeight();
        if(offset > _textViewLog.getHeight()){
            _textViewLog.scrollTo(0,offset - _textViewLog.getHeight());
        }
    }
}
