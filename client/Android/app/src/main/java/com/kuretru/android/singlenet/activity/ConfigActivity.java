package com.kuretru.android.singlenet.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.kuretru.android.singlenet.R;
import com.kuretru.android.singlenet.api.service.SinglenetApiService;
import com.kuretru.android.singlenet.constant.SystemConstants;
import com.kuretru.android.singlenet.entity.ServerConfig;
import com.kuretru.android.singlenet.exception.ApiServiceException;
import com.kuretru.android.singlenet.factory.SinglenetApiServiceFactory;
import com.kuretru.android.singlenet.util.ConfigUtils;
import com.kuretru.android.singlenet.util.StringUtils;
import com.kuretru.android.singlenet.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class ConfigActivity extends AppCompatActivity {

    private static final String[] permissionsList = new String[]{
            Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS
    };
    private SharedPreferences sharedPreferences = null;
    private ProgressDialog progressDialog;

    private EditText etServerUrl;
    private EditText etNetworkInterface;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etAuthToken;
    private RadioGroup rgServerType;
    private LinearLayout llUsername;
    private LinearLayout llPassword;
    private LinearLayout llAuthToken;

    private boolean testSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        sharedPreferences = this.getSharedPreferences("config", MODE_PRIVATE);
        initView();
        setServerConfig(ConfigUtils.loadServerConfig(getApplicationContext()));
    }

    public void btnClear_onClick(View view) {
        if (sharedPreferences == null) {
            ToastUtils.show(getApplicationContext(), getString(R.string.exception_exit));
        }
        ConfigUtils.clearServerConfig(getApplicationContext());
    }

    public void btnTest_onClick(View view) {
        this.testSuccess = false;
        ServerConfig serverConfig = getServerConfig();
        if (!validServerConfig(serverConfig)) {
            return;
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("测试中......");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new Thread(() -> {
            try {
                SinglenetApiService apiService = SinglenetApiServiceFactory.build(serverConfig);
                apiService.ping();
                closeProgressDialog(true);
                runOnUiThread(() -> ToastUtils.show(getApplicationContext(), "与服务器通讯成功！"));
            } catch (ApiServiceException e) {
                closeProgressDialog(false);
                runOnUiThread(() -> {
                    ToastUtils.show(getApplicationContext(), e.getMessage());
                });
            }
        }).start();
    }

    public void btnSave_onClick(View view) {
        if (sharedPreferences == null) {
            ToastUtils.show(getApplicationContext(), getString(R.string.exception_exit));
        }
        ServerConfig serverConfig = getServerConfig();
        if (!validServerConfig(serverConfig)) {
            return;
        }
        if (!testSuccess) {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("该服务器尚未测试通过，确定要保存吗？")
                    .setPositiveButton("确定", (dialog, which) -> {
                        saveServerConfig(serverConfig);
                    })
                    .setNegativeButton("取消", (dialog, which) -> {
                    })
                    .create();
            alertDialog.show();
        } else {
            saveServerConfig(serverConfig);
        }
    }

    private void onRadioGroupChecked(int checkedId) {
        if (checkedId == R.id.rbLuciRpc) {
            llUsername.setVisibility(View.VISIBLE);
            llPassword.setVisibility(View.VISIBLE);
            llAuthToken.setVisibility(View.GONE);
        } else if (checkedId == R.id.rbRestfulApi) {
            llUsername.setVisibility(View.GONE);
            llPassword.setVisibility(View.GONE);
            llAuthToken.setVisibility(View.VISIBLE);
        }
    }

    private boolean validServerConfig(ServerConfig serverConfig) {
        String serverUrl = serverConfig.getServerUrl();
        if (StringUtils.isNullOrBlank(serverUrl)) {
            ToastUtils.show(getApplicationContext(), "路由器接口地址不能为空！");
            etServerUrl.requestFocus();
            return false;
        }
        if (!serverUrl.startsWith("http://") && !serverUrl.startsWith("https://")) {
            ToastUtils.show(getApplicationContext(), "URL地址必须以http(s)://开头！");
            etServerUrl.requestFocus();
            return false;
        }
        if (!serverUrl.endsWith("/")) {
            ToastUtils.show(getApplicationContext(), "URL地址结尾不能含有！");
            etServerUrl.requestFocus();
            return false;
        }

        if (StringUtils.isNullOrBlank(serverConfig.getNetworkInterface())) {
            ToastUtils.show(getApplicationContext(), "路由器接口不能为空！");
            etNetworkInterface.requestFocus();
            return false;
        }

        if (SystemConstants.CONFIG_SERVER_TYPE_LUCI_RPC.equals(serverConfig.getServerType())) {
            if (StringUtils.isNullOrBlank(serverConfig.getUsername()) || StringUtils.isNullOrBlank(serverConfig.getPassword())) {
                ToastUtils.show(getApplicationContext(), "用户名和密码不能为空！");
                etPassword.requestFocus();
                return false;
            }
        } else {
            if (StringUtils.isNullOrBlank(serverConfig.getAuthToken())) {
                ToastUtils.show(getApplicationContext(), "通信密钥不能为空！");
                etAuthToken.requestFocus();
                return false;
            }
        }
        return true;
    }

    /**
     * 从窗体获取服务端配置
     *
     * @return 服务端配置
     */
    private ServerConfig getServerConfig() {
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setServerUrl(etServerUrl.getText().toString().trim());
        serverConfig.setNetworkInterface(etNetworkInterface.getText().toString().trim());
        serverConfig.setUsername(etUsername.getText().toString().trim());
        serverConfig.setPassword(etPassword.getText().toString().trim());
        serverConfig.setAuthToken(etAuthToken.getText().toString().trim());
        if (R.id.rbLuciRpc == rgServerType.getCheckedRadioButtonId()) {
            serverConfig.setServerType(SystemConstants.CONFIG_SERVER_TYPE_LUCI_RPC);
        } else {
            serverConfig.setServerType(SystemConstants.CONFIG_SERVER_TYPE_RESTFUL_API);
        }
        return serverConfig;
    }

    /**
     * 向窗体设置服务端配置
     *
     * @param serverConfig 服务端配置
     */
    private void setServerConfig(ServerConfig serverConfig) {
        this.etServerUrl.setText(serverConfig.getServerUrl());
        this.etNetworkInterface.setText(serverConfig.getNetworkInterface());
        this.etUsername.setText(serverConfig.getUsername());
        this.etPassword.setText(serverConfig.getPassword());
        this.etAuthToken.setText(serverConfig.getAuthToken());
        if (SystemConstants.CONFIG_SERVER_TYPE_LUCI_RPC.equals(serverConfig.getServerType())) {
            this.onRadioGroupChecked(R.id.rbLuciRpc);
            this.rgServerType.check(R.id.rbLuciRpc);
        } else {
            this.onRadioGroupChecked(R.id.rbRestfulApi);
            this.rgServerType.check(R.id.rbRestfulApi);
        }
    }

    /**
     * 保存服务端配置至配置文件
     *
     * @param serverConfig 服务端配置
     */
    private void saveServerConfig(ServerConfig serverConfig) {
        ConfigUtils.saveServerConfig(getApplicationContext(), serverConfig);
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
        this.etServerUrl = this.findViewById(R.id.etServerUrl);
        this.etNetworkInterface = this.findViewById(R.id.etNetworkInterface);
        this.etUsername = this.findViewById(R.id.etUsername);
        this.etPassword = this.findViewById(R.id.etPassword);
        this.etAuthToken = this.findViewById(R.id.etAuthToken);
        this.rgServerType = this.findViewById(R.id.rgServerType);
        this.llUsername = this.findViewById(R.id.llUsername);
        this.llPassword = this.findViewById(R.id.llPassword);
        this.llAuthToken = this.findViewById(R.id.llAuthToken);

        rgServerType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onRadioGroupChecked(checkedId);
            }
        });
    }

}
