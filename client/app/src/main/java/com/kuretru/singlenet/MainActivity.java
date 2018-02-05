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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

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
                    loadNextTime();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _singlenetHandler = new SinglenetHandler(this, _logHandler);
        initView();
        loadConfig();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadConfig();
        loadLog();
        loadNextTime();
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
        _editTextServer.setText(url);
        _editTextSecret.setText(pwd);
        loadLog();
    }

    private void toastShow(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void buttonGoOnClick(View view) {
        _singlenetHandler.setParameters(_editTextServer.getText().toString(),
                _editTextSecret.getText().toString());
        _singlenetHandler.finished = false;
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
        alarmHelper.setNextAlarm();
        LogHelper.LogD("注册定时任务成功");
        toastShow("注册定时任务成功");
        loadNextTime();
    }

    public void buttonClearOnClick(View view) {
        SharedPreferences settings = getSharedPreferences(LogHelper.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("log", "");
        editor.commit();
        loadLog();
    }

    private void loadLog() {
        SharedPreferences settings = getSharedPreferences(LogHelper.PREFS_NAME, 0);
        String log = settings.getString("log", "").trim() + "\n";
        _textViewLog.setText(log);
        int offset = _textViewLog.getLineCount() * _textViewLog.getLineHeight();
        if (offset > _textViewLog.getHeight()) {
            _textViewLog.scrollTo(0, offset - _textViewLog.getHeight());
        }
    }

    private void loadNextTime() {
        SharedPreferences settings = getSharedPreferences(LogHelper.PREFS_NAME, 0);
        String time = settings.getString("nextTime", "");
        SimpleDateFormat formatter = new SimpleDateFormat("yy.MM.dd HH:mm:ss");
        try {
            Date next = formatter.parse(time);
            Date now = new Date(System.currentTimeMillis());
            if (next.getTime() - now.getTime() > 0) {
                _textViewTime.setText("下一次任务时间：\n" + time);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
