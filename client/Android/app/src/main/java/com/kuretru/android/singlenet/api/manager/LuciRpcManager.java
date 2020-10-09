package com.kuretru.android.singlenet.api.manager;

import com.kuretru.android.singlenet.entity.LuciRpcResponse;
import com.kuretru.android.singlenet.exception.ApiServiceException;

import retrofit2.Call;

public interface LuciRpcManager {

    /**
     * 登录路由器，获取AuthToken
     *
     * @throws ApiServiceException API服务调用异常
     */
    void remoteLogin() throws ApiServiceException;

    /**
     * 获取路由器拨号用户名
     *
     * @return 服务调用
     */
    Call<LuciRpcResponse> getUsername();

    /**
     * 设置路由器拨号用户名
     *
     * @param username 拨号用户名
     * @return 服务调用
     */
    Call<LuciRpcResponse> setUsername(String username);

    /**
     * 获取路由器拨号密码
     *
     * @return 拨号密码
     */
    Call<LuciRpcResponse> getPassword();

    /**
     * 设置路由器拨号密码
     *
     * @param password 拨号密码
     * @return 服务调用
     */
    Call<LuciRpcResponse> setPassword(String password);

    /**
     * 获取路由器网络接口状态
     *
     * @return 服务调用
     */
    Call<LuciRpcResponse> getInterfaceStatus();

    /**
     * 启用网络接口
     *
     * @return 服务调用
     */
    Call<LuciRpcResponse> setInterfaceUp();

}
