package com.kuretru.android.singlenet.util;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuretru.android.singlenet.constant.SystemConstants;
import com.kuretru.android.singlenet.entity.RestfulApiResponse;
import com.kuretru.android.singlenet.entity.ServerConfig;
import com.kuretru.android.singlenet.exception.ApiServiceException;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;

public class RetrofitUtils {

    private static final String TAG = "KT-RetrofitUtils";

    /**
     * 同步执行请求
     *
     * @param call 封装好的请求
     * @param <R>  响应实体类型
     * @return 响应实体
     * @throws ApiServiceException API服务调用异常
     */
    public static <R> R syncExecute(Call<R> call) throws ApiServiceException {
        try {
            Response<R> execute = call.execute();
            if (execute.isSuccessful()) {
                R response = execute.body();
                return response;
            } else {
                String body = execute.errorBody().string();
                if (StringUtils.isNullOrBlank(body)) {
                    throw new ApiServiceException("AccessToken已过期");
                }
                RestfulApiResponse<String> response = getErrorResponse(body);
                throw new ApiServiceException(String.format("服务调用失败[%s]：%s", response.getMessage(), response.getData()));
            }
        } catch (IOException e) {
            throw new ApiServiceException(e.getMessage());
        }
    }

    /**
     * 根据ServerConfig配置OkHttpClient.Builder
     *
     * @param serverConfig ServerConfig
     * @return OkHttpClient.Builder
     */
    public static OkHttpClient.Builder okHttpClientBuilder(ServerConfig serverConfig) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS);
        if (SystemConstants.CONFIG_IGNORE.equals(serverConfig.getVerifySsl())) {
            builder = RetrofitUtils.ignoreCertificate(builder);
        }
        builder.addInterceptor(chain -> {
            Request original = chain.request();
            Request request = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(request);
        });
        return builder;
    }

    /**
     * 为OkHttpClient.Build()增加忽略SSL证书验证配置
     *
     * @param builder OkHttpClient.Build()
     * @return OkHttpClient.Build()
     */
    private static OkHttpClient.Builder ignoreCertificate(OkHttpClient.Builder builder) {
        Log.w(TAG, "Ignore Ssl Certificate");
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }};

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            Log.w(TAG, "Exception while configuring IgnoreSslCertificate: " + e.getMessage());
        }
        return builder;
    }

    private static RestfulApiResponse<String> getErrorResponse(String body) throws ApiServiceException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(body, new TypeReference<RestfulApiResponse<String>>() {
            });
        } catch (IOException e) {
            throw new ApiServiceException(String.format("Json反序列化失败[%s]：%s", e.getMessage(), body));
        }
    }

}
