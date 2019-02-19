package com.kuretru.android.singlenet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerConfig {

    private String url;

    private String secret;

}
