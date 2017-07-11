package com.dream.dreamview.module.multi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dream.dreamview.R;
import com.dream.dreamview.RequestConstant;
import com.dream.dreamview.base.NavBaseActivity;
import com.dream.dreamview.module.multi.api.FirstItem;
import com.dream.dreamview.module.multi.api.M;
import com.dream.dreamview.module.multi.api.N;
import com.dream.dreamview.module.multi.api.P;
import com.dream.dreamview.module.multi.api.SecItem;
import com.dream.dreamview.module.multi.api.MultiTypeConverterFactory;
import com.dream.dreamview.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hpu.baserecyclerviewadapter.BaseItem;
import com.hpu.baserecyclerviewadapter.BaseRecyclerViewAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * multiType recyclerview sample
 * 以及返回数据封装
 * Created by Administrator on 2017/6/27
 */

public class MultiTypeActivity extends NavBaseActivity {
    private MeiNv nv;
    private BaseRecyclerViewAdapter<BaseItem> adapter;

    @Override
    protected int getContentView() {
        return R.layout.meinv_activity_multi_type;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDisplayHomeAsUpEnabled(true);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseRecyclerViewAdapter<>();
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
                .addConverterFactory(MultiTypeConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(client)
                .build();

        nv = retrofit.create(MeiNv.class);
        getData(0);
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BaseItem item = adapter.getItem(position);
                if (item instanceof FirstItem) {
                    ToastUtil.showShortToast(MultiTypeActivity.this, ((FirstItem) item).mData);
                } else if (item instanceof SecItem) {
                    ToastUtil.showShortToast(MultiTypeActivity.this, ((SecItem) item).mData);
                }
            }
        });

//        String json = "{\"message\":\"success\",\"status\":200,\"data\":[{\"type\":1,\"data\":{\"text\":\"text1111\"}},{\"type\":2,\"data\":{\"url\":\"url222222\"}},{\"type\":1,\"data\":{\"text\":\"text1111\"}},{\"type\":2,\"data\":{\"url\":\"url222222\"}},{\"type\":1,\"data\":{\"text\":\"text1111\"}},{\"type\":2,\"data\":{\"url\":\"url222222\"}},{\"type\":1,\"data\":{\"text\":\"text1111\"}},{\"type\":2,\"data\":{\"url\":\"url222222\"}},{\"type\":1,\"data\":{\"text\":\"text1111\"}},{\"type\":2,\"data\":{\"url\":\"url222222\"}}]}";
//        Gson gson = new Gson();
//        Multi test = gson.fromJson(json, Multi.class);
//        List<M> data = test.data;
//        for(M m : data) {
//            if (m.type == 1) {
//                Type type = new TypeToken<P>() {}.getType();
//                P p = gson.fromJson(gson.toJson(m.data), type);
//                LogUtil.d("结果P：" + p.text);
//            } else {
//                Type type = new TypeToken<N>() {}.getType();
//                N n = gson.fromJson(gson.toJson(m.data), type);
//                LogUtil.d("结果N：" + n.url);
//            }
//        }
    }

    public void getData(int p) {
        nv.getPic("美女", "少妇", 0, p, 30, "channel", 1).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<M>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<M> value) {
                        Gson gson = new Gson();
                        List<BaseItem> list = new ArrayList<>();
                        for (M m : value) {
                            if (m.type == 1) {
                                Type type = new TypeToken<P>() {
                                }.getType();
                                P p = gson.fromJson(gson.toJson(m.data), type);
                                list.add(new FirstItem(p.text));
                            } else {
                                Type type = new TypeToken<N>() {
                                }.getType();
                                N n = gson.fromJson(gson.toJson(m.data), type);
                                list.add(new SecItem(n.url));
                            }
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

    interface MeiNv {
        @GET(RequestConstant.IMG_LIST)
        Observable<List<M>> getPic(@Query("col") String col, @Query("tag") String tag,
                                   @Query("sort") int sort, @Query("pn") int pn,
                                   @Query("rn") int rn, @Query("p") String p, @Query("from") int from);
    }
}
