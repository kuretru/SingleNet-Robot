package com.kuretru.singlenet;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SmsHelper {

    private static final String PHONE_NUMBER = "1065930051";
    private static final String SMS_CONTENT = "MM";
    private Uri SMS_INBOX = Uri.parse("content://sms");
    private Context _context;
    private SmsManager _smsManager;
    private SmsObserver _smsObserver;
    private long StartTime;
    private Handler _parentHandler;
    private Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                String code = (String) msg.obj;
                _context.getContentResolver().unregisterContentObserver(_smsObserver);
                Message message = _parentHandler.obtainMessage(1, code);
                _parentHandler.sendMessage(message);
            }
        }
    };

    public SmsHelper(Context context, Handler handler) {
        _context = context;
        _parentHandler = handler;
        _smsManager = SmsManager.getDefault();
        _smsObserver = new SmsObserver(context, _handler);
    }

    private void sendMeesage(String phoneNumber, String smsContent) {
        _smsManager.sendTextMessage(phoneNumber, null, smsContent, null, null);
    }

    public void sendSxSms() {
        StartTime = System.currentTimeMillis();
        sendMeesage(PHONE_NUMBER, SMS_CONTENT);
        _context.getContentResolver().registerContentObserver(SMS_INBOX, true, _smsObserver);
        Toast.makeText(_context, "成功发送短信", Toast.LENGTH_SHORT).show();
    }
}
