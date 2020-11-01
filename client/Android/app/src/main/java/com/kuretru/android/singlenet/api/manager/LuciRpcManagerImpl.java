package com.kuretru.android.singlenet.api.manager;

import com.kuretru.android.singlenet.api.mapper.LuciRpcMapper;
import com.kuretru.android.singlenet.entity.LuciRpcRequest;
import com.kuretru.android.singlenet.entity.LuciRpcResponse;
import com.kuretru.android.singlenet.entity.ServerConfig;
import com.kuretru.android.singlenet.exception.ApiServiceException;
import com.kuretru.android.singlenet.factory.LuciRpcRequestFactory;
import com.kuretru.android.singlenet.util.RetrofitUtils;
import com.kuretru.android.singlenet.util.StringUtils;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class LuciRpcManagerImpl implements LuciRpcManager {

    private final ServerConfig serverConfig;
    private final LuciRpcMapper mapper;
    private String authToken;

    public LuciRpcManagerImpl(ServerConfig serverConfig) throws ApiServiceException {
        this.serverConfig = serverConfig;

        OkHttpClient.Builder okHttpClient = RetrofitUtils.okHttpClientBuilder(serverConfig);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverConfig.getServerUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(okHttpClient.build())
                .build();
        mapper = retrofit.create(LuciRpcMapper.class);

        remoteLogin();
    }

    @Override
    public void remoteLogin() throws ApiServiceException {
        LuciRpcRequest request = LuciRpcRequestFactory.auth(serverConfig.getUsername(), serverConfig.getPassword());
        Call<LuciRpcResponse> call = mapper.auth(request);
        LuciRpcResponse response = RetrofitUtils.syncExecute(call);
        if (StringUtils.isNullOrEmpty(response.getResult())) {
            throw new ApiServiceException("获取AuthToken失败，密码错误");
        }
        this.authToken = response.getResult().trim();
    }

    @Override
    public Call<LuciRpcResponse> getUsername() {
        LuciRpcRequest request = LuciRpcRequestFactory.getUsername(serverConfig.getNetworkInterface());
        return mapper.uci(request, authToken);
    }

    @Override
    public Call<LuciRpcResponse> setUsername(String username) {
        LuciRpcRequest request = LuciRpcRequestFactory.setUsername(serverConfig.getNetworkInterface(), username);
        return mapper.uci(request, authToken);
    }

    @Override
    public Call<LuciRpcResponse> getPassword() {
        LuciRpcRequest request = LuciRpcRequestFactory.getPassword(serverConfig.getNetworkInterface());
        return mapper.uci(request, authToken);
    }

    @Override
    public Call<LuciRpcResponse> setPassword(String password) {
        LuciRpcRequest request = LuciRpcRequestFactory.setPassword(serverConfig.getNetworkInterface(), password);
        return mapper.uci(request, authToken);
    }

    @Override
    public Call<LuciRpcResponse> commit() {
        LuciRpcRequest request = LuciRpcRequestFactory.commit();
        return mapper.uci(request, authToken);
    }

    @Override
    public Call<LuciRpcResponse> getInterfaceStatus() {
        LuciRpcRequest request = LuciRpcRequestFactory.getInterfaceStatus(serverConfig.getNetworkInterface());
        return mapper.sys(request, authToken);
    }

    @Override
    public Call<LuciRpcResponse> setInterfaceUp() {
        LuciRpcRequest request = LuciRpcRequestFactory.setInterfaceUp(serverConfig.getNetworkInterface());
        return mapper.sys(request, authToken);
    }

}
