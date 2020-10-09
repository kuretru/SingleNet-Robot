package com.kuretru.android.singlenet.api.service;


import com.kuretru.android.singlenet.api.manager.LuciRpcManager;
import com.kuretru.android.singlenet.api.manager.LuciRpcManagerImpl;
import com.kuretru.android.singlenet.entity.InterfaceStatusEnum;
import com.kuretru.android.singlenet.entity.LuciRpcResponse;
import com.kuretru.android.singlenet.entity.NetworkOption;
import com.kuretru.android.singlenet.entity.config.LuciRpcServerConfig;
import com.kuretru.android.singlenet.exception.ApiServiceException;
import com.kuretru.android.singlenet.util.RetrofitUtils;
import com.kuretru.android.singlenet.util.StringUtils;

import java.util.Map;

import retrofit2.Call;

public class LuciRpcApiServiceImpl implements SinglenetApiService {

    private final LuciRpcManager service;

    public LuciRpcApiServiceImpl(LuciRpcServerConfig serverConfig) throws ApiServiceException {
        service = new LuciRpcManagerImpl(serverConfig);
    }

    @Override
    public void ping() throws ApiServiceException {
        service.remoteLogin();
    }

    @Override
    public NetworkOption getNetworkOption() throws ApiServiceException {
        NetworkOption result = new NetworkOption();

        LuciRpcResponse response = RetrofitUtils.syncExecute(service.getUsername());
        if (StringUtils.isNullOrBlank(response.getResult())) {
            throw new ApiServiceException("路由器拨号用户名为空，请检查路由器拨号接口名称是否正确");
        }
        result.setUsername(response.getResult().trim());

        response = RetrofitUtils.syncExecute(service.getPassword());
        if (StringUtils.isNullOrBlank(response.getResult())) {
            throw new ApiServiceException("路由器拨号密码为空，请检查路由器拨号接口名称是否正确");
        }
        result.setPassword(response.getResult().trim());

        return result;
    }

    @Override
    public NetworkOption setNetworkOption(NetworkOption networkOption) throws ApiServiceException {
        if (!StringUtils.isNullOrBlank(networkOption.getUsername())) {
            Call<LuciRpcResponse> call = service.setUsername(networkOption.getUsername().trim());
            LuciRpcResponse response = RetrofitUtils.syncExecute(call);
            if (!"true".equalsIgnoreCase(response.getResult())) {
                throw new ApiServiceException("更新用户名失败：" + response.getResult());
            }
        }

        if (!StringUtils.isNullOrBlank(networkOption.getPassword())) {
            Call<LuciRpcResponse> call = service.setPassword(networkOption.getPassword().trim());
            LuciRpcResponse response = RetrofitUtils.syncExecute(call);
            if (!"true".equalsIgnoreCase(response.getResult())) {
                throw new ApiServiceException("更新密码失败：" + response.getResult());
            }
        }
        return getNetworkOption();
    }

    @Override
    public InterfaceStatusEnum getInterfaceStatus() throws ApiServiceException {
        LuciRpcResponse response = RetrofitUtils.syncExecute(service.getInterfaceStatus());
        String json = response.getResult().trim().replace("\n", "").replace("\t", "");
        if (StringUtils.isNullOrEmpty(json)) {
            throw new ApiServiceException("接口状态返回空");
        } else if (json.contains("not found")) {
            throw new ApiServiceException("该接口不存在");
        }
        Map<String, Object> data = StringUtils.jsonToMap(json);
        if (data.containsKey("up")) {
            Object up = data.get("up");
            if (up instanceof Boolean && (boolean) up) {
                return InterfaceStatusEnum.UP;
            }
        }
        return InterfaceStatusEnum.DOWN;
    }

}
