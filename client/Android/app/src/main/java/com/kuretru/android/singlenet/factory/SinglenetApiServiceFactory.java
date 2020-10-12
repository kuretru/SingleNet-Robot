package com.kuretru.android.singlenet.factory;

import com.kuretru.android.singlenet.api.service.LuciRpcApiServiceImpl;
import com.kuretru.android.singlenet.api.service.RestfulApiServiceImpl;
import com.kuretru.android.singlenet.api.service.SinglenetApiService;
import com.kuretru.android.singlenet.constant.SystemConstants;
import com.kuretru.android.singlenet.entity.ServerConfig;
import com.kuretru.android.singlenet.exception.ApiServiceException;

public class SinglenetApiServiceFactory {

    public static SinglenetApiService build(ServerConfig serverConfig) throws ApiServiceException {
        if (SystemConstants.CONFIG_SERVER_TYPE_LUCI_RPC.equals(serverConfig.getServerType())) {
            return new LuciRpcApiServiceImpl(serverConfig);
        } else {
            return new RestfulApiServiceImpl(serverConfig);
        }
    }

}
