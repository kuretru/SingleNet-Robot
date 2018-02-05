package com.kuretru.singlenet;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class SinglenetHandler {

    public boolean finished;
    private Context _context;
    private String _url;
    private String _secret;
    private String _password;
    private Handler _parentHandler;
    public Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String code = (String) msg.obj;
            HttpHelper httpHelper;
            switch (msg.what) {
                case 1:
                    _password = code;
                    toastShow("Step1 获取到闪讯密码：" + code);
                    httpHelper = new HttpHelper(handler);
                    httpHelper.getRouterPassword(_url);
                    break;
                case 2:
                    toastShow("Step2 当前路由器密码：" + code);
                    if (!_password.equals(code)) {
                        httpHelper = new HttpHelper(handler);
                        httpHelper.setRouterPassword(_url, _password, _secret);
                    } else {
                        LogHelper.LogFile(_context, String.format("获取密码：%1$s，未更新\n", code));
                        loadLog();
                        finished = true;
                    }
                    break;
                case 3:
                    toastShow("Step3 成功设置路由器密码：" + code);
                    LogHelper.LogFile(_context, String.format("获取密码：%1$s，已更新\n", code));
                    loadLog();
                    finished = true;
                    break;
            }
        }
    };

    public SinglenetHandler(Context context, String url, String secret, Handler parentHandler) {
        finished = false;
        _context = context;
        _url = url;
        _secret = secret;
        _parentHandler = parentHandler;
    }

    //在主窗体显示Toast
    private void toastShow(String text) {
        if (_context == null)
            return;
        Toast.makeText(_context, text, Toast.LENGTH_SHORT).show();
    }

    //使主窗体刷新日志
    private void loadLog() {
        if (_parentHandler == null)
            return;
        Message msg = _parentHandler.obtainMessage(1);
        _parentHandler.sendMessage(msg);
    }
}
