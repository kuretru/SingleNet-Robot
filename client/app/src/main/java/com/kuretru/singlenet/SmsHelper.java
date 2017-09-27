package com.kuretru.singlenet;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;

public class SmsHelper {
    
    public static final String PHONE_NUMBER = "18668088408";
    private static final String SMS_CONTENT = "MM";
    private Uri SMS_INBOX = Uri.parse("content://sms");
    private Context _context;
    private SmsManager _smsManager;
    private SmsObserver _smsObserver;
    public long StartTime;
    private Handler _handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1) {
                String code = (String) msg.obj;
                _context.getContentResolver().unregisterContentObserver(_smsObserver);
            }
        }
    };

    public SmsHelper(Context context){
        _context = context;
        _smsManager = SmsManager.getDefault();
        _smsObserver = new SmsObserver(context,_handler);
    }

    private void sendMeesage(String phoneNumber, String smsContent){
        _smsManager.sendTextMessage(phoneNumber, null, smsContent, null, null);
    }

    public void sendSxSms(){
        StartTime = System.currentTimeMillis();
        sendMeesage(PHONE_NUMBER,SMS_CONTENT);
        _context.getContentResolver().registerContentObserver(SMS_INBOX, true, _smsObserver);
    }

}
