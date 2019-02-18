package com.kuretru.android.singlenet.entity;

public class ServerConfig {

    private String url;

    private String secret;

    public ServerConfig() {
        super();
    }

    public ServerConfig(String url, String secret) {
        this.url = url;
        this.secret = secret;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

}
