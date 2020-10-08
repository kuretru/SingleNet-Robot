package com.kuretru.android.singlenet.api.mapper;

import com.kuretru.android.singlenet.entity.LuciRpcRequest;
import com.kuretru.android.singlenet.entity.LuciRpcResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LuciRpcMapper {

    @POST("/cgi-bin/luci/rpc/auth")
    Call<LuciRpcResponse> auth(@Body LuciRpcRequest request);

    @POST("/cgi-bin/luci/rpc/uci")
    Call<LuciRpcResponse> getUsername(@Body LuciRpcRequest request, @Query("auth") String authToken);

    @POST("/cgi-bin/luci/rpc/uci")
    Call<LuciRpcResponse> setUsername(@Body LuciRpcRequest request, @Query("auth") String authToken);

    @POST("/cgi-bin/luci/rpc/uci")
    Call<LuciRpcResponse> getPassword(@Body LuciRpcRequest request, @Query("auth") String authToken);

    @POST("/cgi-bin/luci/rpc/uci")
    Call<LuciRpcResponse> setPassword(@Body LuciRpcRequest request, @Query("auth") String authToken);

    @POST("/cgi-bin/luci/rpc/sys")
    Call<LuciRpcResponse> ifStatus(@Body LuciRpcRequest request, @Query("auth") String authToken);

}
