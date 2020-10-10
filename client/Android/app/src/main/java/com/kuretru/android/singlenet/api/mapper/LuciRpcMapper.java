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
    Call<LuciRpcResponse> uci(@Body LuciRpcRequest request, @Query("auth") String authToken);

    @POST("/cgi-bin/luci/rpc/sys")
    Call<LuciRpcResponse> sys(@Body LuciRpcRequest request, @Query("auth") String authToken);

}
