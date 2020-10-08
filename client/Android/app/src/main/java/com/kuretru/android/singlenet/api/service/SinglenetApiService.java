package com.kuretru.android.singlenet.api.service;

import com.kuretru.android.singlenet.entity.NetworkOption;
import com.kuretru.android.singlenet.exception.ApiServiceException;

public interface SinglenetApiService {

    /**
     * 测试服务器的可用性
     *
     * @throws ApiServiceException API服务调用异常
     */
    void ping() throws ApiServiceException;

    /**
     * 获取服务端网络接口配置
     *
     * @return 服务端网络接口配置
     * @throws ApiServiceException API服务调用异常
     */
    NetworkOption getNetworkOption() throws ApiServiceException;

    /**
     * 设置服务端网络接口配置
     *
     * @param networkOption
     * @return 服务端网络接口配置
     * @throws ApiServiceException API服务调用异常
     */
    NetworkOption setNetworkOption(NetworkOption networkOption) throws ApiServiceException;

}
