package com.kuretru.android.singlenet.api.service;

import com.kuretru.android.singlenet.api.manager.RestfulApiManager;
import com.kuretru.android.singlenet.api.manager.RestfulApiManagerImpl;
import com.kuretru.android.singlenet.entity.InterfaceStatusEnum;
import com.kuretru.android.singlenet.entity.NetworkOption;
import com.kuretru.android.singlenet.entity.RestfulApiResponse;
import com.kuretru.android.singlenet.entity.ServerConfig;
import com.kuretru.android.singlenet.exception.ApiServiceException;
import com.kuretru.android.singlenet.util.RetrofitUtils;
import com.kuretru.android.singlenet.util.SinglenetUtils;

import java.util.Map;

public class RestfulApiServiceImpl implements SinglenetApiService {

    private final RestfulApiManager manager;

    public RestfulApiServiceImpl(ServerConfig serverConfig) {
        manager = new RestfulApiManagerImpl(serverConfig);
    }

    @Override
    public void ping() throws ApiServiceException {
        RetrofitUtils.syncExecute(manager.ping());
    }

    @Override
    public NetworkOption getNetworkOption() throws ApiServiceException {
        RestfulApiResponse<NetworkOption> response = RetrofitUtils.syncExecute(manager.getNetworkOption());
        return response.getData();
    }

    @Override
    public NetworkOption setNetworkOption(NetworkOption networkOption) throws ApiServiceException {
        RestfulApiResponse<NetworkOption> response = RetrofitUtils.syncExecute(manager.setNetworkOption(networkOption));
        return response.getData();
    }

    @Override
    public InterfaceStatusEnum getInterfaceStatus() throws ApiServiceException {
        RestfulApiResponse<Map<String, Object>> response = RetrofitUtils.syncExecute(manager.getInterfaceStatus());
        return SinglenetUtils.parseInterfaceStatus(response.getData());
    }

    @Override
    public InterfaceStatusEnum setInterfaceUp() throws ApiServiceException {
        RestfulApiResponse<Map<String, Object>> response = RetrofitUtils.syncExecute(manager.setInterfaceUp());
        return SinglenetUtils.parseInterfaceStatus(response.getData());
    }

}
