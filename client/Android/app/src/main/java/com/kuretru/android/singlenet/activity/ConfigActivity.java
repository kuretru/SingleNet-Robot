package com.kuretru.android.singlenet.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.kuretru.android.singlenet.R;
import com.kuretru.android.singlenet.api.ApiManager;
import com.kuretru.android.singlenet.entity.ServerConfig;
import com.kuretru.android.singlenet.util.StringUtils;
import com.kuretru.android.singlenet.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class ConfigActivity extends AppCompatActivity {

    private static final String[] permissionsList = new String[]{
            Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS
    };
    private boolean testSuccess = false;
    private TextView etUrl;
    private TextView etSecret;
    private SharedPreferences sharedPreferences = null;
    private ProgressDialog progressDialog;

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
        this.testSuccess = false;
        ServerConfig serverConfig = loadServerConfig();
        if (!checkServerConfig(serverConfig)) {
            return;
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("测试中......");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        ApiManager apiManager = new ApiManager(serverConfig);
        apiManager.ping(this);
    }

    public void btnSave_onClick(View view) {
        if (sharedPreferences == null) {
            ToastUtils.show(getApplicationContext(), getString(R.string.exception_exit));
        }
        ServerConfig serverConfig = loadServerConfig();
        if (!checkServerConfig(serverConfig)) {
            return;
        }
        if (!testSuccess) {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("该服务器尚未测试通过，确定要保存吗？")
                    .setPositiveButton("确定", (dialog, which) -> {
                        try {
                            ApiManager apiManager = new ApiManager(serverConfig);
                            saveServerConfig(serverConfig);
                        } catch (Exception e) {
                            ToastUtils.show(getApplicationContext(), e.getMessage());
                        }
                    })
                    .setNegativeButton("取消", (dialog, which) -> {

                    })
                    .create();
            alertDialog.show();
        } else {
            saveServerConfig(serverConfig);
        }
    }

    private ServerConfig loadServerConfig() {
        String url = etUrl.getText().toString().trim();
        String secret = etSecret.getText().toString().trim();
        if (!StringUtils.isNullOrEmpty(url)) {
            if (!url.endsWith("/")) {
                url = url + "/";
            }
        }
        return new ServerConfig(url, secret);
    }

    private boolean checkServerConfig(ServerConfig serverConfig) {
        String url = serverConfig.getUrl();
        if (StringUtils.isNullOrEmpty(url)) {
            ToastUtils.show(getApplicationContext(), "路由器接口地址不能为空！");
            etUrl.requestFocus();
            return false;
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            ToastUtils.show(getApplicationContext(), "URL地址必须以http(s)://开头！");
            etUrl.requestFocus();
            return false;
        }
        String secret = serverConfig.getSecret();
        if (StringUtils.isNullOrEmpty(secret)) {
            ToastUtils.show(getApplicationContext(), "接口密码不能为空！");
            etSecret.requestFocus();
            return false;
        }
        return true;
    }

    private void saveServerConfig(ServerConfig serverConfig) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("config_url", serverConfig.getUrl());
        editor.putString("config_secret", serverConfig.getSecret());
        editor.apply();
        ToastUtils.show(getApplicationContext(), "配置信息保存成功！");

        checkPermission();
        this.finish();
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

    public void closeProgressDialog(boolean success) {
        progressDialog.cancel();
        this.testSuccess = success;
    }

    private void initView() {
        this.etUrl = this.findViewById(R.id.etUrl);
        this.etSecret = this.findViewById(R.id.etSecret);
        etUrl.setText(sharedPreferences.getString("config_url", ""));
        etSecret.setText(sharedPreferences.getString("config_secret", ""));
    }

}
