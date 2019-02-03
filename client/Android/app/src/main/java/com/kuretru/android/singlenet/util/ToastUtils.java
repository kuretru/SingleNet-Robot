package com.kuretru.android.singlenet.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    public static void show(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

}
