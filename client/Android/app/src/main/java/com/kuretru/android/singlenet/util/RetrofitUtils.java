package com.kuretru.android.singlenet.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuretru.android.singlenet.entity.RestfulApiResponse;
import com.kuretru.android.singlenet.exception.ApiServiceException;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class RetrofitUtils {

    /**
     * 同步执行请求
     *
     * @param call 封装好的请求
     * @param <R>  响应实体类型
     * @return 响应实体
     * @throws ApiServiceException API服务调用异常
     */
    public static <R> R syncExecute(Call<R> call) throws ApiServiceException {
        try {
            Response<R> execute = call.execute();
            if (execute.isSuccessful()) {
                R response = execute.body();
                return response;
            } else {
                String body = execute.errorBody().string();
                if (StringUtils.isNullOrBlank(body)) {
                    throw new ApiServiceException("AccessToken已过期");
                }
                RestfulApiResponse<String> response = getErrorResponse(body);
                throw new ApiServiceException(String.format("服务调用失败[%s]：%s", response.getMessage(), response.getData()));
            }
        } catch (IOException e) {
            throw new ApiServiceException(e.getMessage());
        }
    }

    private static RestfulApiResponse<String> getErrorResponse(String body) throws ApiServiceException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(body, new TypeReference<RestfulApiResponse<String>>() {
            });
        } catch (IOException e) {
            throw new ApiServiceException(String.format("Json反序列化失败[%s]：%s", e.getMessage(), body));
        }
    }

}
