package com.dream.dreamview.net;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;


/**
 * Created by Administrator on 2017/6/15.
 */

public class ServiceGenerator {
    public static final String API_URL = "https://api.github.com";

    public ServiceGenerator() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public okhttp3.Response intercept(Chain chain) throws IOException {
//                        Request request = chain.request();
//                        okhttp3.Response response = chain.proceed(request);
//                        Log.e("++++++++++", response.code() + "");
//                        Log.e("++++++++++", response.body().string());
//                        return chain.proceed(chain.request().newBuilder().header("t", "sdfdsf").build());
//                    }
//                })
//                .authenticator(new Authenticator() {
//                    @Nullable
//                    @Override
//                    public Request authenticate(Route route, okhttp3.Response response) throws IOException {
//                        Log.e("++++=======", response.code() + "");
//                        return response.request();
//                    }
//                })
                .connectTimeout(15000, TimeUnit.MILLISECONDS)
                .readTimeout(5000, TimeUnit.MILLISECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(CustomConverterFactory.create())
                .client(client)
                .build();
    }

}
