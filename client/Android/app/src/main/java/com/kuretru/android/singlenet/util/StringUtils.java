package com.kuretru.android.singlenet.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuretru.android.singlenet.entity.ApiResponse;

import java.io.IOException;

import okhttp3.ResponseBody;

public class StringUtils {

    public static boolean isNullOrEmpty(String text) {
        return text == null || text.isEmpty();
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
