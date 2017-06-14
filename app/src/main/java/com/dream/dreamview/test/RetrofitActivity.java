package com.dream.dreamview.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dream.dreamview.R;
import com.dream.dreamview.base.NavBaseActivity;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();


        // Create an instance of our GitHub API interface.
        GitHub github = retrofit.create(GitHub.class);

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


        call.enqueue(new Callback<List<Contributor>>() {
            @Override
            public void onResponse(Call<List<Contributor>> call, Response<List<Contributor>> response) {
                Log.e("============", response.body().toString());
            }

            @Override
            public void onFailure(Call<List<Contributor>> call, Throwable t) {

            }
        });

    }

    public static final String API_URL = "https://api.github.com";

    public static class Contributor {
        public final String login;
        public final int contributions;

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
    }
}
