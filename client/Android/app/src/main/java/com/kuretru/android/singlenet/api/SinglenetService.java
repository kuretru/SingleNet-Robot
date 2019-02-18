package com.kuretru.android.singlenet.api;

import com.kuretru.android.singlenet.entity.ApiResponse;
import com.kuretru.android.singlenet.entity.WanOption;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface SinglenetService {

    @GET(".")
    Call<ApiResponse> ping();

    @GET("wan_option")
    Call<ApiResponse> getWanOption();

    @PUT("wan_option")
    Call<ApiResponse> setWanOption(@Body WanOption wanOption);

}
