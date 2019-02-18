package com.kuretru.android.singlenet.entity;

public class ApiResponse {

    public static final Integer SUCCESS = 2000;

    public static final Integer FAILURE = 4000;

    private Integer code;

    private String message;

    private Object data;

    public ApiResponse() {
        super();
    }

    public ApiResponse(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ApiResponse failure(String data) {
        return new ApiResponse(FAILURE, "failure", data);
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
