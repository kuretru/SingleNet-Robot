package com.kuretru.android.singlenet.entity;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import lombok.Data;

@Data
public class SystemLog extends LitePalSupport {

    private Integer id;

    @Column(nullable = false)
    private String time;

    @Column(nullable = false, defaultValue = "No message")
    private String message;

}
