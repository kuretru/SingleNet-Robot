package com.kuretru.android.singlenet.entity;

import java.util.List;

import lombok.Data;

@Data
public class ServerConfig {

    /** 服务端地址，合理的地址：'http://192.168.1.1:8080' */
    private String serverUrl;

    /** 服务端地址历史数据 */
    private List<String> serverUrlHistory;

    /** 忽略SSL证书验证 */
    private String verifySsl;

    /** 网络接口名称(OpenWrt默认值为'wan') */
    private String networkInterface;

    /** 服务端类型 */
    private String serverType;

    /** OpenWrt登录用户名(默认值为'root') */
    private String username;

    /** OpenWrt登录密码 */
    private String password;

    /** 服务端通信密钥 */
    private String authToken;

    /** 发送短信的SIM卡 */
    private String simCard;

}
