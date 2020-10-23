package com.kuretru.android.singlenet.api.manager;

import com.kuretru.android.singlenet.api.mapper.RestfulApiMapper;
import com.kuretru.android.singlenet.entity.NetworkOption;
import com.kuretru.android.singlenet.entity.RestfulApiRequest;
import com.kuretru.android.singlenet.entity.RestfulApiResponse;
import com.kuretru.android.singlenet.entity.ServerConfig;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RestfulApiManagerImpl implements RestfulApiManager {

    private final ServerConfig serverConfig;
    private final RestfulApiMapper mapper;

    public RestfulApiManagerImpl(ServerConfig serverConfig) {
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
        mapper = retrofit.create(RestfulApiMapper.class);
    }

    @Override
    public Call<RestfulApiResponse<String>> ping() {
        return mapper.ping(serverConfig.getAuthToken());
    }

    @Override
    public Call<RestfulApiResponse<NetworkOption>> getNetworkOption() {
        return mapper.getNetworkOption(
                serverConfig.getAuthToken(),
                serverConfig.getNetworkInterface()
        );
    }

    @Override
    public Call<RestfulApiResponse<NetworkOption>> setNetworkOption(NetworkOption networkOption) {
        RestfulApiRequest.NetworkOptionRequest request = new RestfulApiRequest.NetworkOptionRequest();
        request.setNetworkInterface(serverConfig.getNetworkInterface());
        request.setUsername(networkOption.getUsername());
        request.setPassword(networkOption.getPassword());
        return mapper.setNetworkOption(serverConfig.getAuthToken(), request);
    }

    @Override
    public Call<RestfulApiResponse<Map<String, Object>>> getInterfaceStatus() {
        return mapper.getInterfaceStatus(
                serverConfig.getAuthToken(),
                serverConfig.getNetworkInterface()
        );
    }

    @Override
    public Call<RestfulApiResponse<Map<String, Object>>> setInterfaceUp() {
        RestfulApiRequest.InterfaceStatusRequest request = new RestfulApiRequest.InterfaceStatusRequest();
        request.setNetworkInterface(serverConfig.getNetworkInterface());
        return mapper.setInterfaceUp(serverConfig.getAuthToken(), request);
    }

}
