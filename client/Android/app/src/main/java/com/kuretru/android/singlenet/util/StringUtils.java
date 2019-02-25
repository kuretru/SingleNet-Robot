package com.kuretru.android.singlenet.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuretru.android.singlenet.entity.ApiResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;

public class StringUtils {

    public static final String DEBUG_TAG = "singlenet-robot-debug";

    public static boolean isNullOrEmpty(String text) {
        return text == null || text.isEmpty();
    }

    public static String timestampToString(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
        return format.format(new Date(timestamp));
    }

    public static ApiResponse<String> getErrorResponse(ResponseBody errorBody) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = errorBody.string();
            return mapper.readValue(json, new TypeReference<ApiResponse<String>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
