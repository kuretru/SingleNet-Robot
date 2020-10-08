package com.kuretru.android.singlenet.api.manager;

import com.kuretru.android.singlenet.api.mapper.LuciRpcMapper;
import com.kuretru.android.singlenet.entity.LuciRpcRequest;
import com.kuretru.android.singlenet.entity.LuciRpcResponse;
import com.kuretru.android.singlenet.entity.config.LuciRpcServerConfig;
import com.kuretru.android.singlenet.exception.ApiServiceException;
import com.kuretru.android.singlenet.factory.LuciRpcRequestFactory;
import com.kuretru.android.singlenet.util.RetrofitUtils;
import com.kuretru.android.singlenet.util.StringUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class LuciRpcManagerImpl implements LuciRpcManager {

    private final LuciRpcServerConfig serverConfig;
    private final LuciRpcMapper mapper;
    private String authToken;

    public LuciRpcManagerImpl(LuciRpcServerConfig serverConfig) throws ApiServiceException {
        this.serverConfig = serverConfig;

        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS);
        okHttpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request request = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(request);
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverConfig.getServerUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(okHttpClient.build())
                .build();
        mapper = retrofit.create(LuciRpcMapper.class);

        authToken = getAuthToken();
    }

    @Override
    public String getAuthToken() throws ApiServiceException {
        LuciRpcRequest request = LuciRpcRequestFactory.auth(serverConfig.getUsername(), serverConfig.getPassword());
        Call<LuciRpcResponse> call = mapper.auth(request);
        LuciRpcResponse response = RetrofitUtils.syncExecute(call);
        if (StringUtils.isNullOrEmpty(response.getResult())) {
            throw new ApiServiceException("获取AuthToken失败");
        }
        return response.getResult().trim();
    }

    @Override
    public Call<LuciRpcResponse> getUsername() {
        LuciRpcRequest request = LuciRpcRequestFactory.getUsername(serverConfig.getNetworkInterface());
        Call<LuciRpcResponse> call = mapper.getUsername(request, authToken);
        return call;
    }

    @Override
    public Call<LuciRpcResponse> setUsername(String username) {
        return null;
    }

    @Override
    public Call<LuciRpcResponse> getPassword() {
        LuciRpcRequest request = LuciRpcRequestFactory.getPassword(serverConfig.getNetworkInterface());
        Call<LuciRpcResponse> call = mapper.getPassword(request, authToken);
        return call;
    }

    @Override
    public Call<LuciRpcResponse> setPassword(String password) {
        return null;
    }

    @Override
    public Call<LuciRpcResponse> ifStatus() {
        LuciRpcRequest request = LuciRpcRequestFactory.ifStatus(serverConfig.getNetworkInterface());
        Call<LuciRpcResponse> call = mapper.ifStatus(request, authToken);
        return call;
    }

    @Override
    public Call<LuciRpcResponse> ifUp() {
        return null;
    }

}
