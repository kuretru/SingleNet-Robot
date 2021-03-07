package com.kuretru.android.singlenet.worker;

import android.content.Context;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.kuretru.android.singlenet.constant.SystemConstants;
import com.kuretru.android.singlenet.receiver.SmsReceiver;
import com.kuretru.android.singlenet.util.ConfigUtils;
import com.kuretru.android.singlenet.util.StringUtils;
import com.kuretru.android.singlenet.util.SubscriptionUtils;

public class SendSmsWorker extends Worker {

    private static final String TAG = "KT_SendSmsWorker";
    private static final String SINGLENET_MOBILE = "1065930051";
    private static final String SINGLENET_MESSAGE = "MM";

    public SendSmsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        IntentFilter filter = new IntentFilter();
        filter.addAction(SmsReceiver.ACTION);
        context.registerReceiver(new SmsReceiver(), filter);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(TAG, "开始发送获取闪讯密码短信");
        String simCard = ConfigUtils.loadServerConfig(this.getApplicationContext()).getSimCard();
        SmsManager smsManager = null;
        if (StringUtils.isNullOrBlank(simCard) || SystemConstants.CONFIG_SIM_CARD_DEFAULT.equals(simCard)) {
            smsManager = SmsManager.getDefault();
        } else {
            int slotId = Integer.parseInt(simCard.substring(simCard.length() - 1));
            int subId = SubscriptionUtils.getSubId(this.getApplicationContext(), slotId - 1);
            if (-1 == subId) {
                Log.e(TAG, "卡槽" + slotId + "中不存在SIM卡");
                return Result.failure();
            } else if (-2 == subId) {
                Log.e(TAG, "没有READ_PHONE_STATE权限");
                return Result.failure();
            }
            smsManager = SmsManager.getSmsManagerForSubscriptionId(subId);
        }
        smsManager.sendTextMessage(SINGLENET_MOBILE, null, SINGLENET_MESSAGE, null, null);
        Log.i(TAG, "成功发送获取闪讯密码短信");
        return Result.success();
    }

}
