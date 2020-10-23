package com.kuretru.android.singlenet.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

public class RestfulApiRequest {

    @Data
    public static class NetworkOptionRequest {

        @JsonProperty("interface")
        private String networkInterface;

        private String username;

        private String password;

    }

    @Data
    public static class InterfaceStatusRequest {

        @JsonProperty("interface")
        private String networkInterface;

    }

}
