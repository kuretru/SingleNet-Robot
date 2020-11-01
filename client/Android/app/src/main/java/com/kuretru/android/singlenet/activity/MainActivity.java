package com.kuretru.android.singlenet.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.google.common.util.concurrent.ListenableFuture;
import com.kuretru.android.singlenet.R;
import com.kuretru.android.singlenet.api.service.SinglenetApiService;
import com.kuretru.android.singlenet.constant.SystemConstants;
import com.kuretru.android.singlenet.entity.InterfaceStatusEnum;
import com.kuretru.android.singlenet.entity.NetworkOption;
import com.kuretru.android.singlenet.entity.ServerConfig;
import com.kuretru.android.singlenet.entity.SystemLog;
import com.kuretru.android.singlenet.exception.ApiServiceException;
import com.kuretru.android.singlenet.factory.SinglenetApiServiceFactory;
import com.kuretru.android.singlenet.service.AlarmService;
import com.kuretru.android.singlenet.util.ConfigUtils;
import com.kuretru.android.singlenet.util.StringUtils;
import com.kuretru.android.singlenet.util.ToastUtils;
import com.kuretru.android.singlenet.worker.SendSmsWorker;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "KT_MainActivity";

    private Button btnSend;
    private Button btnAlarm;
    private Button btnCancel;
    private Button btnUpdate;
    private TextView tvAlarm;
    private EditText etUsername;
    private EditText etPassword;
    private ListView listView;

    private Context context;
    private ServerConfig serverConfig = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LitePal.initialize(this);
        setContentView(R.layout.activity_main);
        context = this.getApplicationContext();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadServerConfig();
        loadWorkStatus();
        loadLog();
    }

    public void btnConfig_onClick(View view) {
        Intent intent = new Intent(this, ConfigActivity.class);
        this.startActivity(intent);
    }

    public void btnSend_onClick(View view) {
        WorkRequest workRequest = OneTimeWorkRequest.from(SendSmsWorker.class);
        WorkManager.getInstance(this).enqueue(workRequest);
        ToastUtils.show(context, "获取闪讯密码短信发送成功！");
    }

    public void btnAlarm_onClick(View view) {
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest
                .Builder(SendSmsWorker.class, 15, TimeUnit.MINUTES)
                .setInitialDelay(1, TimeUnit.MINUTES)
                .build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                SystemConstants.SINGLENET_WORKER_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest);
        loadWorkStatus();
        ToastUtils.show(context, "注册定时任务成功！");
    }

    public void btnCancel_onClick(View view) {
        WorkManager.getInstance(this).cancelUniqueWork(SystemConstants.SINGLENET_WORKER_NAME);
        loadWorkStatus();
        ToastUtils.show(context, "取消定时任务成功！");
    }

    public void btnUpdate_onClick(View view) {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (StringUtils.isNullOrBlank(password)) {
            ToastUtils.show(context, "密码是必填项！");
            return;
        }
        ProgressDialog progressDialog = ProgressDialog.show(this, "提示", "更新中");
        NetworkOption networkOption = new NetworkOption(username, password);
        new Thread(() -> {
            try {
                SinglenetApiService apiService = SinglenetApiServiceFactory.build(serverConfig);
                apiService.setNetworkOption(networkOption);
                runOnUiThread(() -> {
                    String message = StringUtils.isNullOrEmpty(username) ? "" : "用户名及";
                    ToastUtils.show(context, "更新" + message + "密码成功！");
                });
                InterfaceStatusEnum status = apiService.getInterfaceStatus();
                if (status == InterfaceStatusEnum.DOWN) {
                    apiService.setInterfaceUp();
                }
            } catch (ApiServiceException e) {
                runOnUiThread(() -> {
                    ToastUtils.show(getApplicationContext(), "更新失败：" + e.getMessage());
                });
            } finally {
                progressDialog.cancel();
            }
        }).start();
    }

    public void btnClearLog_onClick(View view) {
        LitePal.deleteAll(SystemLog.class);
        loadLog();
    }

    private void initView() {
        this.btnSend = findViewById(R.id.btnSend);
        this.btnAlarm = findViewById(R.id.btnAlarm);
        this.btnCancel = findViewById(R.id.btnCancel);
        this.btnUpdate = findViewById(R.id.btnUpdate);
        this.tvAlarm = findViewById(R.id.tvAlarm);
        this.etUsername = findViewById(R.id.etUsername);
        this.etPassword = findViewById(R.id.etPassword);
        this.listView = findViewById(R.id.listView);
    }

    private void loadServerConfig() {
        ServerConfig serverConfig = ConfigUtils.loadServerConfig(this.getApplicationContext());
        if (StringUtils.isNullOrEmpty(serverConfig.getServerUrl()) || StringUtils.isNullOrEmpty(serverConfig.getNetworkInterface())) {
            btnSend.setEnabled(false);
            btnAlarm.setEnabled(false);
            btnCancel.setEnabled(false);
            btnUpdate.setEnabled(false);
            this.serverConfig = null;
            ToastUtils.show(context, "未检测到服务器配置，请配置服务器！");
        } else {
            btnSend.setEnabled(true);
            btnAlarm.setEnabled(true);
            btnCancel.setEnabled(true);
            btnUpdate.setEnabled(true);
            this.serverConfig = serverConfig;
        }
    }

    private void loadAlarmStatus() {
        AlarmService alarmService = new AlarmService(context);
        if (alarmService.isRegistered()) {
            long nextTime = alarmService.getNextTime();
            tvAlarm.setText(("下一次任务时间：" + StringUtils.timestampToString(nextTime)));
        } else {
            tvAlarm.setText("定时任务未注册！！");
        }
    }

    private void loadWorkStatus() {
        ListenableFuture<List<WorkInfo>> works = WorkManager.getInstance(this)
                .getWorkInfosForUniqueWork(SystemConstants.SINGLENET_WORKER_NAME);
        try {
            List<WorkInfo> worksInfo = works.get();
            if (worksInfo.isEmpty()) {
                tvAlarm.setText("定时任务未注册！");
                return;
            }
            WorkInfo work = worksInfo.get(0);
            switch (work.getState()) {
                case ENQUEUED:
                    tvAlarm.setText("定时任务已启用！");
                    break;
                case RUNNING:
                    tvAlarm.setText("定时任务正在运行！");
                    break;
                case CANCELLED:
                    tvAlarm.setText("定时任务已取消！");
                    break;
                default:
                    tvAlarm.setText("定时任务状态未知！");
                    break;
            }
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "查询定时任务状态错误：" + e.getMessage());
        }
    }

    private void loadLog() {
        List<SystemLog> logs = LitePal.limit(10).order("id desc").find(SystemLog.class);
        List<String> data = new ArrayList<>(logs.size());
        for (SystemLog systemLog : logs) {
            data.add(systemLog.getTime() + " " + systemLog.getMessage());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);
    }

}
