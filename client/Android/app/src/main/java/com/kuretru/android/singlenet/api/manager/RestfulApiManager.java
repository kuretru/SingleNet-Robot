package com.kuretru.android.singlenet.api.manager;

import com.kuretru.android.singlenet.entity.NetworkOption;
import com.kuretru.android.singlenet.entity.RestfulApiResponse;

import java.util.Map;

import retrofit2.Call;

public interface RestfulApiManager {

    /**
     * 测试服务端可用性
     *
     * @return 服务调用
     */
    Call<RestfulApiResponse<String>> ping();

    /**
     * 获取路由器网络接口配置
     *
     * @return 服务调用
     */
    Call<RestfulApiResponse<NetworkOption>> getNetworkOption();

    /**
     * 设置路由器网络接口配置
     *
     * @return 服务调用
     */
    Call<RestfulApiResponse<NetworkOption>> setNetworkOption(NetworkOption networkOption);

    /**
     * 获取路由器网络接口状态
     *
     * @return 服务调用
     */
    Call<RestfulApiResponse<Map<String, Object>>> getInterfaceStatus();

    /**
     * 启用网络接口
     *
     * @return 服务调用
     */
    Call<RestfulApiResponse<Map<String, Object>>> setInterfaceUp();

}
