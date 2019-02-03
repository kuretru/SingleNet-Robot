package com.kuretru.android.singlenet.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kuretru.android.singlenet.R;
import com.kuretru.android.singlenet.util.StringUtils;
import com.kuretru.android.singlenet.util.ToastUtils;

public class ConfigActivity extends AppCompatActivity {

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

    public void btnSave_onClick(View view) {
        if (sharedPreferences == null) {
            ToastUtils.show(getApplicationContext(), getString(R.string.exception_exit));
        }
        String url = etUrl.getText().toString();
        String secret = etSecret.getText().toString();
        if (StringUtils.isNullOrEmpty(url)) {
            ToastUtils.show(getApplicationContext(), "路由器接口地址不能为空！");
            etUrl.requestFocus();
            return;
        }
        if (StringUtils.isNullOrEmpty(secret)) {
            ToastUtils.show(getApplicationContext(), "接口密码不能为空！");
            etSecret.requestFocus();
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("config_url", url);
        editor.putString("config_secret", secret);
        editor.apply();
        ToastUtils.show(getApplicationContext(), "配置信息保存成功！");
    }

    public void btnExit_onClick(View view) {
        this.finish();
    }

    private void initView() {
        this.etUrl = this.findViewById(R.id.etUrl);
        this.etSecret = this.findViewById(R.id.etSecret);
        etUrl.setText(sharedPreferences.getString("config_url", ""));
        etSecret.setText(sharedPreferences.getString("config_secret", ""));
    }

}
