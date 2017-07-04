package com.dream.dreamview.net;

import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;

import com.dream.dreamview.AppConstant;
import com.dream.dreamview.BuildConfig;
import com.dream.dreamview.MyApplication;
import com.dream.dreamview.util.LogUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Retrofit 封装
 * Created by Administrator on 2017/6/15
 */

public class ServiceGenerator {
    private static final String API_BASE_URL = BuildConfig.API_BASE_URL;
    private static final boolean DEBUG = AppConstant.DEBUG;
    private static final int CONNECT_TIME_OUT = 15000;
    private static final int READ_TIME_OUT = 5000;
    private static final int CACHE_COUNT = 2;
    private LruCache<Class<?>, Map<String, Object>> mServeces;

    private ServiceGenerator() {
        mServeces = new LruCache<>(CACHE_COUNT);
    }

    private static class ServiceGeneratorLoader {
        private static final ServiceGenerator INSTANCE = new ServiceGenerator();
    }

    public static ServiceGenerator getInstance() {
        return ServiceGeneratorLoader.INSTANCE;
    }

    public <T> T createService(Class<T> service, boolean token) {
        Map<String, Object> cache = mServeces.get(service);
        String key = token + API_BASE_URL;
        if (cache != null) {
            T t = (T) cache.get(key);
            if (t != null) {
                return t;
            }
        } else {
            cache = new HashMap<>();
        }
        Retrofit retrofit = getRetrofit(token);
        T t = retrofit.create(service);
        cache.put(key, t);
        mServeces.put(service, cache);
        return t;
    }

    private Retrofit getRetrofit(boolean token) {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        okHttpBuilder.connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS);
        okHttpBuilder.readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS);
        if (DEBUG) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpBuilder.addInterceptor(httpLoggingInterceptor);
        } else {
            // 添加拦截器
            okHttpBuilder.authenticator(new Authenticator() {
                @Nullable
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    // 这是 OKHttp 符合标准的 验证身份过期(比如token过期)的拦截器 错误码：401
                    return response.request();
                }
            });
            okHttpBuilder.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    // 这是根据返回状态码，或者返回信息 来处理Token过期问题
                    Request request = chain.request();
                    okhttp3.Response response = chain.proceed(request);
                    LogUtil.e("HTTP 状态码" + response.code());
                    LogUtil.e("接口返回信息", response.body().string());
                    return chain.proceed(chain.request().newBuilder().header("t", "sdfdsf").build());
                }
            });
            // 请求头拦截器
            ApiInterceptor apiInterceptor = new ApiInterceptor(MyApplication.getContext(), token);
            okHttpBuilder.addInterceptor(apiInterceptor);
        }
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(API_BASE_URL);
        builder.client(okHttpBuilder.build());
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()));
        return builder.build();
    }

}
