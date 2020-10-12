package com.kuretru.android.singlenet.api.service;

import com.kuretru.android.singlenet.entity.InterfaceStatusEnum;
import com.kuretru.android.singlenet.entity.NetworkOption;
import com.kuretru.android.singlenet.entity.ServerConfig;
import com.kuretru.android.singlenet.exception.ApiServiceException;

public class RestfulApiServiceImpl implements SinglenetApiService {

    public RestfulApiServiceImpl(ServerConfig serverConfig) {

    }

    @Override
    public void ping() throws ApiServiceException {

    }

    @Override
    public NetworkOption getNetworkOption() throws ApiServiceException {
        return null;
    }

    @Override
    public NetworkOption setNetworkOption(NetworkOption networkOption) throws ApiServiceException {
        return null;
    }

    @Override
    public InterfaceStatusEnum getInterfaceStatus() throws ApiServiceException {
        return null;
    }

    @Override
    public InterfaceStatusEnum setInterfaceUp() throws ApiServiceException {
        return null;
    }

}
