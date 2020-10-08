package com.kuretru.android.singlenet.entity.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LuciRpcServerConfig extends BaseServerConfig {

    /**
     * OpenWrt系统登录用户名(OpenWrt默认值为'root')
     */
    private String username;

    /**
     * OpenWrt系统登录密码
     */
    private String password;

}
