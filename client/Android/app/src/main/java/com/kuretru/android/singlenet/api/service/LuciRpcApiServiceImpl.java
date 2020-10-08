package com.kuretru.android.singlenet.api.service;

import com.kuretru.android.singlenet.api.manager.LuciRpcManager;
import com.kuretru.android.singlenet.api.manager.LuciRpcManagerImpl;
import com.kuretru.android.singlenet.entity.LuciRpcResponse;
import com.kuretru.android.singlenet.entity.NetworkOption;
import com.kuretru.android.singlenet.entity.config.LuciRpcServerConfig;
import com.kuretru.android.singlenet.exception.ApiServiceException;
import com.kuretru.android.singlenet.util.RetrofitUtils;
import com.kuretru.android.singlenet.util.StringUtils;

public class LuciRpcApiServiceImpl implements SinglenetApiService {

    private final LuciRpcManager service;

    public LuciRpcApiServiceImpl(LuciRpcServerConfig serverConfig) throws ApiServiceException {
        service = new LuciRpcManagerImpl(serverConfig);
    }

    @Override
    public void ping() throws ApiServiceException {

    }

    @Override
    public NetworkOption getNetworkOption() throws ApiServiceException {
        NetworkOption result = new NetworkOption();

        LuciRpcResponse response = RetrofitUtils.syncExecute(service.getUsername());
        if (StringUtils.isNullOrEmpty(response.getResult())) {
            throw new ApiServiceException("路由器拨号用户名为空，请检查路由器拨号接口名称是否正确");
        }
        result.setUsername(response.getResult().trim());

        response = RetrofitUtils.syncExecute(service.getPassword());
        if (StringUtils.isNullOrEmpty(response.getResult())) {
            throw new ApiServiceException("路由器拨号密码为空，请检查路由器拨号接口名称是否正确");
        }
        result.setPassword(response.getResult().trim());

        return result;
    }

    @Override
    public NetworkOption setNetworkOption(NetworkOption networkOption) throws ApiServiceException {
        return null;
    }

}
