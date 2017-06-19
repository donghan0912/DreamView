package com.dream.dreamview.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.dream.dreamview.R;
import com.dream.dreamview.base.NavBaseActivity;
import com.dream.dreamview.net.CustomConverterFactory;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;
import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2017/6/14
 */

public class RetrofitActivity extends NavBaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.test_activity_retrofit;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        okhttp3.Response response = chain.proceed(request);
                        Log.e("++++++++++", response.code() + "");
                        Log.e("++++++++++", response.body().string());
                        return chain.proceed(chain.request().newBuilder().header("t", "sdfdsf").build());
                    }
                })
                .authenticator(new Authenticator() {
                    @Nullable
                    @Override
                    public Request authenticate(Route route, okhttp3.Response response) throws IOException {
                        Log.e("++++=======", response.code() + "");
                        return response.request();
                    }
                })
                .connectTimeout(15000, TimeUnit.MILLISECONDS)
                .readTimeout(5000, TimeUnit.MILLISECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(CustomConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(client)
                .build();


        // Create an instance of our GitHub API interface.
        GitHub github = retrofit.create(GitHub.class);

        // Gson使用
        List list = new ArrayList();
        list.add("11111");
        list.add("2222");
        Map map = new HashMap();
        map.put("key", list);
        Gson gson = new Gson();
        String abc = gson.toJson("abc");
        String s = gson.toJson(abc);
        String s1 = gson.toJson(list);
        Log.e("======", abc + "|--------|" + s + "|--------|" + s1);

        String json1 = "{\"code\":\"0\",\"message\":\"success\",\"data\":{}}";
        String json2 = "{\"code\":\"0\",\"message\":\"success\",\"data\":[]}";
//        D d = JSON.parseObject(json1, D.class);
//        D d = gson.fromJson(json1, D.class);


//        D[] d2 = gson.fromJson(json2, D[].class);
//        Log.e("======", d2.code + "|--------|" + d2.messge + "|--------|" + d2.data);

        // Create a call instance for looking up Retrofit contributors.
        final Call<List<Contributor>> call = github.contributors("square", "retrofit");

        // Fetch and print a list of the contributors to the library.

//
//        Runnable runnable = new Runnable() {
//            public void run() {
//                List<Contributor> contributors = null;
//                try {
//                    contributors = call.execute().body();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                for (Contributor contributor : contributors) {
//                    Log.e("============", contributor.login + " (" + contributor.contributions + ")");
//                }
//            }
//        };
//        new Thread(runnable).start();


//        call.enqueue(new Callback<List<Contributor>>() {
//            @Override
//            public void onResponse(Call<List<Contributor>> call, Response<List<Contributor>> response) {
//                Log.e("============", response.body().toString());
//            }
//
//            @Override
//            public void onFailure(Call<List<Contributor>> call, Throwable t) {
//
//            }
//        });
        github.contributor()
                .subscribe(new Subject<BaseRes<List<M>>>() {
                    @Override
                    public boolean hasObservers() {
                        return false;
                    }

                    @Override
                    public boolean hasThrowable() {
                        return false;
                    }

                    @Override
                    public boolean hasComplete() {
                        return false;
                    }

                    @Override
                    public Throwable getThrowable() {
                        return null;
                    }

                    @Override
                    protected void subscribeActual(Observer<? super BaseRes<List<M>>> observer) {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRes<List<M>> value) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public static final String API_URL = "https://api.github.com";

    public static class Contributor {
        public String login;
        public int contributions;
        public int s;

        public Contributor(String login, int contributions) {
            this.login = login;
            this.contributions = contributions;
        }
    }

    public interface GitHub {
        @GET("/repos/{owner}/{repo}/contributors")
        Call<List<Contributor>> contributors(
                @Path("owner") String owner,
                @Path("repo") String repo);

        @GET("http://tj.nineton.cn/Heart/index/future24h/?city=CHSH000000")
        Observable<BaseRes<List<M>>> contributor();
    }

    class D {
        @SerializedName("data")
        public String data;
    }

    class BaseRes<T> {

        public String status;
        public T hourly;
    }

    class M {
        public String text;
        public String code;
        public String temperature;
        public String time;
    }
}
