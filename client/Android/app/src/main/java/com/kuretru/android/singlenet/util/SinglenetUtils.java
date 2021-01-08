package com.kuretru.android.singlenet.util;

import com.kuretru.android.singlenet.entity.InterfaceStatusEnum;

import java.util.Map;

public class SinglenetUtils {

    public static InterfaceStatusEnum parseInterfaceStatus(Map<String, Object> data) {
        if (data == null) {
            return InterfaceStatusEnum.DOWN;
        }
        if (data.containsKey("up")) {
            Object up = data.get("up");
            if (up instanceof Boolean && (boolean) up) {
                return InterfaceStatusEnum.UP;
            }
        }
        return InterfaceStatusEnum.DOWN;
    }

}
