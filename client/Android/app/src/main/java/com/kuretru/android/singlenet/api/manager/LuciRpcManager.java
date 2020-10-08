package com.kuretru.android.singlenet.api.manager;

import com.kuretru.android.singlenet.entity.LuciRpcResponse;
import com.kuretru.android.singlenet.exception.ApiServiceException;

import retrofit2.Call;

public interface LuciRpcManager {

    String getAuthToken() throws ApiServiceException;

    Call<LuciRpcResponse> getUsername();

    Call<LuciRpcResponse> setUsername(String username);

    Call<LuciRpcResponse> getPassword();

    Call<LuciRpcResponse> setPassword(String password);

    Call<LuciRpcResponse> ifStatus();

    Call<LuciRpcResponse> ifUp();

}
