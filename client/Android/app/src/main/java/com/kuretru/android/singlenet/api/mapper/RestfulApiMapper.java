package com.kuretru.android.singlenet.api.mapper;

import com.kuretru.android.singlenet.entity.NetworkOption;
import com.kuretru.android.singlenet.entity.RestfulApiRequest;
import com.kuretru.android.singlenet.entity.RestfulApiResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestfulApiMapper {

    @GET("/api/ping")
    Call<RestfulApiResponse<String>> ping(@Header("Access-Token") String accessToken);

    @GET("/api/network/option")
    Call<RestfulApiResponse<NetworkOption>> getNetworkOption(
            @Header("Access-Token") String accessToken,
            @Query("interface") String networkInterface
    );

    @POST("/api/network/option")
    Call<RestfulApiResponse<NetworkOption>> setNetworkOption(
            @Header("Access-Token") String accessToken,
            @Body RestfulApiRequest.NetworkOptionRequest request
    );

    @GET("/api/network/status")
    Call<RestfulApiResponse<Map<String, Object>>> getInterfaceStatus(
            @Header("Access-Token") String accessToken,
            @Query("interface") String networkInterface
    );

    @POST("/api/network/status")
    Call<RestfulApiResponse<Map<String, Object>>> setInterfaceUp(
            @Header("Access-Token") String accessToken,
            @Body RestfulApiRequest.InterfaceStatusRequest request
    );

}
