package com.kuretru.android.singlenet.api;

import android.content.Context;

import com.kuretru.android.singlenet.activity.ConfigActivity;
import com.kuretru.android.singlenet.entity.ApiResponse;
import com.kuretru.android.singlenet.entity.ServerConfig;
import com.kuretru.android.singlenet.entity.WanOption;
import com.kuretru.android.singlenet.util.StringUtils;
import com.kuretru.android.singlenet.util.ToastUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ApiManager {

    public static final int PING = 1;
    public static final int GET_WAN_OPTION = 2;
    public static final int SET_WAN_OPTION = 3;

    private Retrofit retrofit;
    private SinglenetApi singlenetApi;

    public ApiManager(ServerConfig serverConfig) {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS);
        okHttpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request request = original.newBuilder()
                    .header("Api-Token", serverConfig.getSecret())
                    .header("Accept", "application/json")
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(request);
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(serverConfig.getUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(okHttpClient.build())
                .build();
        singlenetApi = retrofit.create(SinglenetApi.class);
    }

    public Call<ApiResponse<String>> ping() {
        Call<ApiResponse<String>> call = singlenetApi.ping();
        return call;
    }

    public void ping(Context context) {
        ConfigActivity configActivity = (ConfigActivity) context;
        Call<ApiResponse<String>> call = this.ping();
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                configActivity.closeProgressDialog(response.isSuccessful());
                if (response.isSuccessful()) {
                    ToastUtils.show(context, "与服务器通讯成功！");
                    return;
                }
                ApiResponse<String> errorResponse = StringUtils.getErrorResponse(response.errorBody());
                ToastUtils.show(context, errorResponse.getData());
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                configActivity.closeProgressDialog(false);
                ToastUtils.show(context, "连接失败：" + t.getMessage());
            }
        });
    }

    public Call<ApiResponse<WanOption>> getWanOption() {
        Call<ApiResponse<WanOption>> call = singlenetApi.getWanOption();
        return call;
    }

    public Call<ApiResponse<WanOption>> setWanOption(WanOption wanOption) {
        Call<ApiResponse<WanOption>> call = singlenetApi.setWanOption(wanOption);
        return call;
    }

}
