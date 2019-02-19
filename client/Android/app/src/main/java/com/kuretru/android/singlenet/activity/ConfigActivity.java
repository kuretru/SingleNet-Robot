package com.kuretru.android.singlenet.activity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.kuretru.android.singlenet.R;
import com.kuretru.android.singlenet.api.ApiManager;
import com.kuretru.android.singlenet.entity.ApiResponse;
import com.kuretru.android.singlenet.entity.ServerConfig;
import com.kuretru.android.singlenet.util.StringUtils;
import com.kuretru.android.singlenet.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfigActivity extends AppCompatActivity {

    private static final String[] permissionsList = new String[]{
            Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS
    };
    private TextView etUrl;
    private TextView etSecret;
    private SharedPreferences sharedPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        sharedPreferences = this.getSharedPreferences("config", MODE_PRIVATE);
        initView();
    }

    public void btnClear_onClick(View view) {
        if (sharedPreferences == null) {
            ToastUtils.show(getApplicationContext(), getString(R.string.exception_exit));
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("config_url");
        editor.remove("config_secret");
        editor.apply();
        initView();
    }

    public void btnTest_onClick(View view) {
        String url = etUrl.getText().toString();
        String secret = etSecret.getText().toString();
        if (!checkContent(url, secret)) {
            return;
        }
        if (!url.endsWith("/")) {
            url = url + "/";
        }
        ServerConfig serverConfig = new ServerConfig(url, secret);
        ApiManager apiManager = new ApiManager(serverConfig);
        Call<ApiResponse<String>> call = apiManager.ping();
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                ApiResponse<String> apiResponse = response.body();
                if (ApiResponse.SUCCESS.equals(apiResponse.getCode())) {
                    ToastUtils.show(getApplicationContext(), "与服务器通讯成功！");
                } else {
                    ToastUtils.show(getApplicationContext(), apiResponse.getData());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                ToastUtils.show(getApplicationContext(), "连接失败：" + t.getMessage());
            }
        });
    }

    public void btnSave_onClick(View view) {
        if (sharedPreferences == null) {
            ToastUtils.show(getApplicationContext(), getString(R.string.exception_exit));
        }
        String url = etUrl.getText().toString();
        String secret = etSecret.getText().toString();
        if (!checkContent(url, secret)) {
            return;
        }
        if (!url.endsWith("/")) {
            url = url + "/";
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("config_url", url);
        editor.putString("config_secret", secret);
        editor.apply();
        ToastUtils.show(getApplicationContext(), "配置信息保存成功！");

        checkPermission();
        this.finish();
    }

    private boolean checkContent(String url, String secret) {
        if (StringUtils.isNullOrEmpty(url)) {
            ToastUtils.show(getApplicationContext(), "路由器接口地址不能为空！");
            etUrl.requestFocus();
            return false;
        }
        if (StringUtils.isNullOrEmpty(secret)) {
            ToastUtils.show(getApplicationContext(), "接口密码不能为空！");
            etSecret.requestFocus();
            return false;
        }
        return true;
    }

    private void checkPermission() {
        List<String> permissions = new ArrayList<>();
        for (String permission : permissionsList) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(permission);
            }
        }
        if (permissions.size() > 0) {
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), 1);
        }
    }

    private void initView() {
        this.etUrl = this.findViewById(R.id.etUrl);
        this.etSecret = this.findViewById(R.id.etSecret);
        etUrl.setText(sharedPreferences.getString("config_url", ""));
        etSecret.setText(sharedPreferences.getString("config_secret", ""));
    }

}
