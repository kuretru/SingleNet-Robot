package com.kuretru.android.singlenet.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.kuretru.android.singlenet.api.ApiManager;
import com.kuretru.android.singlenet.entity.ApiResponse;
import com.kuretru.android.singlenet.entity.ServerConfig;
import com.kuretru.android.singlenet.entity.WanOption;
import com.kuretru.android.singlenet.util.ConfigUtils;
import com.kuretru.android.singlenet.util.StringUtils;
import com.kuretru.android.singlenet.util.ToastUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SinglenetService extends IntentService {

    public SinglenetService() {
        super("SinglenetApi");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        String code = bundle.getString("code");
        WanOption wanOption = new WanOption(null, code);

        ServerConfig serverConfig = ConfigUtils.loadServerConfig(this);
        ApiManager apiManager = new ApiManager(serverConfig);
        Call<ApiResponse<WanOption>> call = apiManager.setWanOption(wanOption);
        call.enqueue(new Callback<ApiResponse<WanOption>>() {
            @Override
            public void onResponse(Call<ApiResponse<WanOption>> call, Response<ApiResponse<WanOption>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<WanOption> apiResponse = response.body();
                    if (code.equals(apiResponse.getData().getPassword())) {
                        toastShow("密码相同未更新！");
                    } else {
                        toastShow("更新密码成功！");
                    }
                    return;
                }
                ApiResponse<String> errorResponse = StringUtils.getErrorResponse(response.errorBody());
                toastShow(errorResponse.getData());
            }

            @Override
            public void onFailure(Call<ApiResponse<WanOption>> call, Throwable t) {
                toastShow("连接失败：" + t.getMessage());
            }
        });
    }

    private void toastShow(String message) {
        new Handler().post(() -> ToastUtils.show(SinglenetService.this, message));
    }

}
