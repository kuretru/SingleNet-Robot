package com.kuretru.android.singlenet.entity;

public class ApiResponse {

    public static final Integer SUCCESS = 2000;

    public static final Integer FAILURE = 4000;

    private Integer code;

    private String message;

    private WanOption data;

    public ApiResponse() {
        super();
    }

    private ApiResponse(Integer code, String message, WanOption data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ApiResponse failure(String message) {
        return new ApiResponse(FAILURE, message, null);
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

    public WanOption getData() {
        return data;
    }

    public void setData(WanOption data) {
        this.data = data;
    }

}
