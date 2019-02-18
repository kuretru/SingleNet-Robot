package com.kuretru.android.singlenet.api;

import android.os.Handler;
import android.os.Message;

import com.kuretru.android.singlenet.entity.ApiResponse;
import com.kuretru.android.singlenet.entity.ServerConfig;
import com.kuretru.android.singlenet.entity.WanOption;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ApiManager {

    public static final int PING = 1;
    public static final int GET_WAN_OPTION = 2;
    public static final int SET_WAN_OPTION = 3;

    private Handler handler;
    private ServerConfig serverConfig;
    private Retrofit retrofit;
    private SinglenetService singlenetService;

    public ApiManager(Handler handler, ServerConfig serverConfig) {
        this.handler = handler;
        this.serverConfig = serverConfig;
        retrofit = new Retrofit.Builder()
                .baseUrl(serverConfig.getUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        singlenetService = retrofit.create(SinglenetService.class);
    }

    public void ping() {
        Call<ApiResponse> call = singlenetService.ping();
        execute(call, PING);
    }

    public void getWanOption() {
        Call<ApiResponse> call = singlenetService.getWanOption();
        execute(call, GET_WAN_OPTION);
    }

    public void setWanOption(WanOption wanOption) {

    }

    public void connect() {

    }

    private void execute(Call<ApiResponse> call, int what) {
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Message.obtain(handler, what, response.body()).sendToTarget();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                String message = "与服务器通信失败，请检查服务器配置或网络连接！";
                ApiResponse response = ApiResponse.failure(message);
                Message.obtain(handler, what, response).sendToTarget();
            }
        });
    }

}
