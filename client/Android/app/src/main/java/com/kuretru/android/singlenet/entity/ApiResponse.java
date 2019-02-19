package com.kuretru.android.singlenet.entity;

import lombok.Data;

@Data
public class ApiResponse<D> {

    public static final Integer SUCCESS = 2000;

    public static final Integer FAILURE = 4000;

    private Integer code;

    private String message;

    private D data;

}
