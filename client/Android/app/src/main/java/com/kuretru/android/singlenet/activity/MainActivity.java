package com.kuretru.android.singlenet.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kuretru.android.singlenet.R;
import com.kuretru.android.singlenet.api.ApiManager;
import com.kuretru.android.singlenet.entity.ApiResponse;
import com.kuretru.android.singlenet.entity.ServerConfig;
import com.kuretru.android.singlenet.entity.WanOption;
import com.kuretru.android.singlenet.service.SmsService;
import com.kuretru.android.singlenet.util.StringUtils;
import com.kuretru.android.singlenet.util.ToastUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button btnSend;
    private Button btnUpdate;
    private EditText etUsername;
    private EditText etPassword;

    private Context context;
    private SharedPreferences sharedPreferences = null;
    private ServerConfig serverConfig = null;
    private ApiManager apiManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this.getApplicationContext();
        sharedPreferences = this.getSharedPreferences("config", MODE_PRIVATE);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadServerConfig();
        if (serverConfig != null) {
            apiManager = new ApiManager(serverConfig);
            apiManager.ping(this.getApplicationContext());
        }
    }

    public void btnConfig_onClick(View view) {
        Intent intent = new Intent(this, ConfigActivity.class);
        this.startActivity(intent);
    }

    public void btnSend_onClick(View view) {
        Intent intent = new Intent(this, SmsService.class);
        this.startService(intent);
        //Test1<WanOption> test1 = mapper.readValue(json, new TypeReference<Test1<WanOption>>(){})
    }

    public void btnUpdate_onClick(View view) {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        if (StringUtils.isNullOrEmpty(password)) {
            ToastUtils.show(context, "密码是必填项！");
            return;
        }
        WanOption wanOption = new WanOption(username, password);
        Call<ApiResponse<WanOption>> call = apiManager.setWanOption(wanOption);
        call.enqueue(new Callback<ApiResponse<WanOption>>() {
            @Override
            public void onResponse(Call<ApiResponse<WanOption>> call, Response<ApiResponse<WanOption>> response) {

            }

            @Override
            public void onFailure(Call<ApiResponse<WanOption>> call, Throwable t) {

            }
        });
    }

    private void initView() {
        this.btnSend = findViewById(R.id.btnSend);
        this.btnUpdate = findViewById(R.id.btnUpdate);
        this.etUsername = findViewById(R.id.etUsername);
        this.etPassword = findViewById(R.id.etPassword);
    }

    private void loadServerConfig() {
        String url = sharedPreferences.getString("config_url", "");
        String secret = sharedPreferences.getString("config_secret", "");
        if (StringUtils.isNullOrEmpty(url) || StringUtils.isNullOrEmpty(secret)) {
            btnSend.setEnabled(false);
            btnUpdate.setEnabled(false);
            serverConfig = null;
            ToastUtils.show(context, "未检测到服务器配置，请配置服务器！");
        } else {
            btnSend.setEnabled(true);
            btnUpdate.setEnabled(true);
            serverConfig = new ServerConfig(url, secret);
        }
    }

}
