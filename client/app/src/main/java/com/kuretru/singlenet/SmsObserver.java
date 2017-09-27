package com.kuretru.singlenet;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsObserver extends ContentObserver {
    private Context _context;
    private Handler _handler;

    public SmsObserver(Context context, Handler handler) {
        super(handler);
        _context = context;
        _handler = handler;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        if (uri.toString().equals("content://sms/raw")) {
            return;
        } else {
            Uri inboxUri = Uri.parse("content://sms/inbox");
            String[] projection = new String[] {"address", "date", "body", "type"};
            String where = " address = '" + "106593005" + "' AND date > " +
                    (System.currentTimeMillis() - 5 * 60 * 1000);
            Cursor cursor = _context.getContentResolver().query(inboxUri, projection, where, null, "date desc");
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    String body = cursor.getString(cursor.getColumnIndex("body"));
                    Pattern pattern = Pattern.compile("[0-9]{6}");
                    Matcher matcher = pattern.matcher(body);
                    if (matcher.find()) {
                        String code = matcher.group(0);
                        Message msg = _handler.obtainMessage(1, code);
                        _handler.sendMessage(msg);
                    }
                }
                cursor.close();
            }
        }
    }
}

