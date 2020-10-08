package com.kuretru.android.singlenet.entity.config;

import lombok.Data;

@Data
public abstract class BaseServerConfig {

    /**
     * 服务端地址
     * 合理的地址：'http://192.168.1.1:8080'
     */
    private String serverUrl;

    /**
     * 网络接口名称(OpenWrt默认值为'wan')
     */
    private String networkInterface;


}
