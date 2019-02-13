package com.kuretru.android.singlenet.entity;

public class ApiResponse {

    public static final Integer SUCCESS = 200;

    public static final Integer FAILURE = 400;

    private Integer code;

    private String message;

    private Object data;

    public ApiResponse() {
        super();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
