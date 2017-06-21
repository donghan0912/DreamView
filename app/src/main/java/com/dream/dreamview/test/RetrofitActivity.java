package com.dream.dreamview.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.dream.dreamview.R;
import com.dream.dreamview.base.NavBaseActivity;
import com.dream.dreamview.net.CustomConverterFactory;
import com.dream.dreamview.net.ResponseException;
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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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

        github.contributor()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<M>>() {
                               @Override
                               public void onSubscribe(Disposable d) {

                               }

                               @Override
                               public void onNext(List<M> value) {
                                   for (M m : value) {
                                       Log.e("code=================: ", m.code);
                                   }
                               }

                               @Override
                               public void onError(Throwable e) {
                                   if (e != null) {
                                       if (e instanceof ResponseException) {
                                           ResponseException exception = (ResponseException) e;
                                           Toast.makeText(getApplicationContext(), exception.message, Toast.LENGTH_SHORT).show();
                                       } else {
                                           Toast.makeText(getApplicationContext(), "网络异常，请检查网络", Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               }

                               @Override
                               public void onComplete() {

                               }
                           }
                );

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
        Observable<List<M>> contributor();
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
