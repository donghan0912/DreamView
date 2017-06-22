package com.dream.dreamview.meinv;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.dream.dreamview.R;
import com.dream.dreamview.RequestConstant;
import com.dream.dreamview.base.NavBaseActivity;
import com.dream.dreamview.net.CustomConverterFactory;
import com.hpu.baserecyclerviewadapter.BaseItem;
import com.hpu.baserecyclerviewadapter.BaseRecyclerViewAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by lenovo on 2017/6/21
 */

public class BeautyActivity extends NavBaseActivity{


    private BaseRecyclerViewAdapter adapter;

    @Override
    protected int getContentView() {
        return R.layout.meinv_activity_beauty;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        adapter = new BaseRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        getData();
    }

    private void getData() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(15000, TimeUnit.MILLISECONDS)
                .readTimeout(5000, TimeUnit.MILLISECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.test")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(client)
                .build();

        MeiNv nv = retrofit.create(MeiNv.class);
        nv.getPic().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response value) {
                        List<BaseItem> list = new ArrayList<>();
//                        List<ImgItem> list = new ArrayList<>();
                        for (Gallery v : value.tngou) {
                            v.height = getRandomHeight();
                            list.add(new ImgItem(v));
                        }
                        adapter.setData(list);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public interface MeiNv {
        @GET(RequestConstant.IMG_LIST)
        Observable<Response> getPic();
    }

    class Response {
        public boolean status;
        public int total;
        public List<Gallery> tngou;
    }

    private int getRandomHeight() {
        return (int) (400 + Math.random() * 100);
    }
}
