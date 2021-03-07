package com.kuretru.android.singlenet.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;

import androidx.core.app.ActivityCompat;

public class SubscriptionUtils {

    private SubscriptionUtils() {
    }

    /**
     * 获取指定SIM卡槽上SIM卡的ID
     *
     * @param context Context
     * @param slotId  SIM卡槽ID(0->卡槽1，1->卡槽2)
     * @return SIM卡的subId，-1为未找到SIM卡，-2为没有权限
     */
    public static int getSubId(Context context, int slotId) {
        SubscriptionManager subscriptionManager = SubscriptionManager.from(context);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            int[] subIds = subscriptionManager.getSubscriptionIds(slotId);
            if (subIds == null || subIds.length == 0) {
                return -1;
            }
            return subIds[0];
        } else {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return -2;
            }
            SubscriptionInfo info = subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(slotId);
            return info.getSubscriptionId();
        }
    }

}
