package com.kuretru.android.singlenet.util;

import android.os.Build;
import android.text.TextUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StringUtils {

    public static boolean isNullOrEmpty(String text) {
        return text == null || text.isEmpty();
    }

    public static boolean isNullOrBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

    public static List<String> stringToList(String data, String splitter) {
        if (data == null) {
            return null;
        }
        return Arrays.asList(data.split(splitter));
    }

    public static String listToString(List<String> data, String splitter) {
        if (data == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return String.join(splitter, data);
        } else {
            return TextUtils.join(splitter, data);
        }
    }

    public static Map<String, Object> jsonToMap(String data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(data, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            return new HashMap<>();
        }
    }

    public static String mapToJson(Map<String, Object> data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    public static String timestampToString(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
        return format.format(new Date(timestamp));
    }

}
