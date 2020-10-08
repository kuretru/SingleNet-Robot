package com.kuretru.android.singlenet.util;

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
                throw new ApiServiceException("服务调用失败：" + body);
            }
        } catch (IOException e) {
            throw new ApiServiceException(e.getMessage());
        }
    }

}
