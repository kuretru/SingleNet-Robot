package com.kuretru.android.singlenet.entity;

import lombok.Data;

@Data
public class RestfulApiResponse<D> {

    private Integer code;

    private String message;

    private D data;

}
