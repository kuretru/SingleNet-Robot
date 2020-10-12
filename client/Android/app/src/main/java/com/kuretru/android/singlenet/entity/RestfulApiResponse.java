package com.kuretru.android.singlenet.entity;

import lombok.Data;

@Data
public class RestfulApiResponse<D> {

    public static final Integer SUCCESS = 2000;

    public static final Integer CREATED = 2001;

    public static final Integer UPDATED = 2002;

    public static final Integer DELETED = 2003;

    public static final Integer FAILURE = 4000;

    private Integer code;

    private String message;

    private D data;

}
