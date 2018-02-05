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

    private String _password;
    TextView _editTextServer;
    TextView _editTextSecret;
    TextView _textViewTime;
    TextView _textViewLog;
    Button _buttonGo;
    Button _buttonSave;
    SinglenetHandler _singlenetHandler;
    Handler _logHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    loadLog();
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

    @Override
    protected void onResume() {
        super.onResume();
        loadConfig();
    }

    private void initView() {
        _editTextServer = (TextView) findViewById(R.id.editTextServer);
        _editTextSecret = (TextView) findViewById(R.id.editTextSecret);
        _textViewTime = (TextView) findViewById(R.id.textViewTime);
        _textViewLog = (TextView) findViewById(R.id.textViewLog);
        _buttonGo = (Button) findViewById(R.id.buttonGo);
        _buttonSave = (Button) findViewById(R.id.buttonSave);
        _textViewLog.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    private void loadConfig() {
        SharedPreferences settings = getSharedPreferences(LogHelper.PREFS_NAME, 0);
        String url = settings.getString("url", "http://dorm.i5zhen.com:8079/sx");
        String pwd = settings.getString("secret", "123456");
        String log = settings.getString("log", "");
        _editTextServer.setText(url);
        _editTextSecret.setText(pwd);
        _textViewLog.setText(log);
    }

    private void toastShow(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void buttonGoOnClick(View view) {
        _singlenetHandler = new SinglenetHandler(getApplicationContext(),
                _editTextServer.getText().toString(), _editTextSecret.getText().toString(),
                _logHandler);
        SmsHelper smsHelper = new SmsHelper(this, _singlenetHandler.handler);
        smsHelper.sendSxSms();
    }

    public void buttonSaveOnClick(View view) {
        SharedPreferences settings = getSharedPreferences(LogHelper.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("url", _editTextServer.getText().toString());
        editor.putString("secret", _editTextSecret.getText().toString());
        editor.commit();
        toastShow("配置保存成功");
    }

    public void buttonAlarmOnClick(View view) {
        AlarmHelper alarmHelper = new AlarmHelper(this);
        LogHelper.LogD("注册定时任务成功");
        toastShow("注册定时任务成功");
        String time = alarmHelper.setNextAlarm();
        _textViewTime.setText("下一次任务时间：\n" + time);
    }

    public void buttonClearOnClick(View view) {
        SharedPreferences settings = getSharedPreferences(LogHelper.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("log", "");
        editor.commit();
        loadLog();
    }

    public void loadLog() {
        SharedPreferences settings = getSharedPreferences(LogHelper.PREFS_NAME, 0);
        String log = settings.getString("log", "");
        _textViewLog.setText(log);
        //_textViewLog.append(text);
        //int offset = _textViewLog.getLineCount() * _textViewLog.getLineHeight();
        //if (offset > _textViewLog.getHeight()) {
        //    _textViewLog.scrollTo(0, offset - _textViewLog.getHeight());
        //}
    }
}
