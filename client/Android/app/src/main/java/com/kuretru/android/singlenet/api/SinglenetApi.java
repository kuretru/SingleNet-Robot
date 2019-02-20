package com.kuretru.android.singlenet.api;

import com.kuretru.android.singlenet.entity.ApiResponse;
import com.kuretru.android.singlenet.entity.WanOption;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface SinglenetApi {

    @GET(".")
    Call<ApiResponse<String>> ping();

    @GET("wan_option")
    Call<ApiResponse<WanOption>> getWanOption();

    @PUT("wan_option")
    Call<ApiResponse<WanOption>> setWanOption(@Body WanOption wanOption);

}
