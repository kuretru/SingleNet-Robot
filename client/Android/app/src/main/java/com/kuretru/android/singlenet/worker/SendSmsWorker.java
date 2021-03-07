package com.kuretru.android.singlenet.worker;

import android.content.Context;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.telephony.SubscriptionManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.kuretru.android.singlenet.constant.SystemConstants;
import com.kuretru.android.singlenet.receiver.SmsReceiver;
import com.kuretru.android.singlenet.util.ConfigUtils;
import com.kuretru.android.singlenet.util.StringUtils;

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
            int slotId = Integer.valueOf(simCard.substring(simCard.length() - 1)) - 1;
            SubscriptionManager subscriptionManager = SubscriptionManager.from(this.getApplicationContext());
            int[] subIds = subscriptionManager.getSubscriptionIds(slotId);
            if (subIds == null || subIds.length == 0) {
                Log.e(TAG, "卡槽" + (slotId + 1) + "中不存在SIM卡");
                return Result.failure();
            }
            int subId = subIds[0];
            smsManager = SmsManager.getSmsManagerForSubscriptionId(subId);
        }
        smsManager.sendTextMessage(SINGLENET_MOBILE, null, SINGLENET_MESSAGE, null, null);
        Log.i(TAG, "成功发送获取闪讯密码短信");
        return Result.success();
    }

}
