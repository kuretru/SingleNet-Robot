package com.kuretru.android.singlenet.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuretru.android.singlenet.entity.RestfulApiResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;

public class StringUtils {

    public static final String DEBUG_TAG = "singlenet-robot-debug";

    public static boolean isNullOrEmpty(String text) {
        return text == null || text.isEmpty();
    }

    public static boolean isNullOrBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

    public static Map<String, Object> jsonToMap(String data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(data, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    public static String mapToJson(Map<String, Object> data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String timestampToString(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
        return format.format(new Date(timestamp));
    }

    public static RestfulApiResponse<String> getErrorResponse(ResponseBody errorBody) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = errorBody.string();
            return mapper.readValue(json, new TypeReference<RestfulApiResponse<String>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
