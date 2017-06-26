package com.dream.dreamview.meinv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.dream.dreamview.R;
import com.dream.dreamview.RequestConstant;
import com.dream.dreamview.base.NavBaseActivity;
import com.dream.dreamview.net.CustomConverterFactory;
import com.dream.dreamview.util.LogUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hpu.baserecyclerviewadapter.BaseItem;
import com.hpu.baserecyclerviewadapter.BaseRecyclerViewAdapter;
import com.hpu.baserecyclerviewadapter.BaseViewHolder;
import com.hpu.baserecyclerviewadapter.EndlessRecyclerOnScrollListener;
import com.hpu.baserecyclerviewadapter.SimpleItem;

import java.io.IOException;
import java.lang.reflect.Type;
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
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by lenovo on 2017/6/21
 */

public class BeautyActivity extends NavBaseActivity{
    private BaseRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private int index = 0;
    private MeiNv nv;

    @Override
    protected int getContentView() {
        return R.layout.meinv_activity_beauty;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        adapter = new BaseRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        getData();
        adapter.setStatusItem(new SimpleItem(R.layout.layout_loading) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                p.setFullSpan(true);
            }
        });

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

        nv = retrofit.create(MeiNv.class);
        getData(index);
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                getData(++index);
            }
        });

        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LargeActivity.start(BeautyActivity.this, ((Gallery) (adapter.getItem(position).mData)).largeUrl);
                int itemViewType = adapter.getItemViewType(position);
                ImgItem item = (ImgItem) adapter.getItem(position);
//                item.mData.
            }
        });


        String json = "{\"message\":\"success\",\"status\":200,\"data\":[{\"type\":1,\"data\":{\"text\":\"text1111\"}},{\"type\":2,\"data\":{\"url\":\"url222222\"}},{\"type\":1,\"data\":{\"text\":\"text1111\"}},{\"type\":2,\"data\":{\"url\":\"url222222\"}},{\"type\":1,\"data\":{\"text\":\"text1111\"}},{\"type\":2,\"data\":{\"url\":\"url222222\"}},{\"type\":1,\"data\":{\"text\":\"text1111\"}},{\"type\":2,\"data\":{\"url\":\"url222222\"}},{\"type\":1,\"data\":{\"text\":\"text1111\"}},{\"type\":2,\"data\":{\"url\":\"url222222\"}}]}";
        Gson gson = new Gson();
        Test test = gson.fromJson(json, Test.class);
        List<M> data = test.data;
        for(M m : data) {
            if (m.type == 1) {
                Type type = new TypeToken<P>() {}.getType();
                P p = gson.fromJson(gson.toJson(m.data), type);
                LogUtil.d("结果P：" + p.text);
            } else {
                Type type = new TypeToken<N>() {}.getType();
                N n = gson.fromJson(gson.toJson(m.data), type);
                LogUtil.d("结果N：" + n.url);
            }
        }
    }

    public void getData(int p) {
        nv.getPic("美女", "少妇", 0, p, 30, "channel", 1).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response value) {
                        List<ImgItem> list = new ArrayList<>();
                        for (Gallery v : value.imgs) {
                            if (!TextUtils.isEmpty(v.url)) {
                                v.height = getRandomHeight();
                                list.add(new ImgItem(BeautyActivity.this, v));
                            }
                        }
                        adapter.removeStatusItem();
                        adapter.addData(list);
                        adapter.setExtraItem(new SimpleItem(R.layout.layout_loadmore) {
                            @Override
                            public void onBindViewHolder(BaseViewHolder holder, int position) {
                                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                                p.setFullSpan(true);
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        adapter.setStatusItem(new SimpleItem(R.layout.layout_error) {
                            @Override
                            public void onBindViewHolder(BaseViewHolder holder, int position) {
                                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                                p.setFullSpan(true);
                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getData(0);
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public interface MeiNv {
        @GET(RequestConstant.IMG_LIST)
        Observable<Response> getPic(@Query("col") String col, @Query("tag") String tag,
                                    @Query("sort") int sort, @Query("pn") int pn,
                                    @Query("rn") int rn, @Query("p") String p, @Query("from") int from);
    }

    class Response {
        public String col;
        public String tag;
        public String tag3;
        public String sort;
        public int totalNum;
        public int startIndex;
        public int returnNumber;
        public List<Gallery> imgs;
    }

    private int getRandomHeight() {
        return (int) (400 + Math.random() * 100);
    }

    class Test {
      public List<M> data;
    }
    class M<T> {
        public int type;
        public T data;
    }
    class N {
        public String url;
    }
    class P {
        public String text;
    }
    private static final String JSON_FROM_SERVICE =
            "[\n" +
                    "    {\n" +
                    "        \"content\":{\n" +
                    "            \"text\":\"A simple text Weibo: JSON_FROM_SERVICE.\",\n" +
                    "            \"content_type\":\"simple_text\"\n" +
                    "        },\n" +
                    "        \"createTime\":\"Just now\",\n" +
                    "        \"user\":{\n" +
                    "            \"avatar\":2130903040,\n" +
                    "            \"name\":\"drakeet\"\n" +
                    "        }\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"content\":{\n" +
                    "            \"resId\":2130837591,\n" +
                    "            \"content_type\":\"simple_image\"\n" +
                    "        },\n" +
                    "        \"createTime\":\"Just now(JSON_FROM_SERVICE)\",\n" +
                    "        \"user\":{\n" +
                    "            \"avatar\":2130903040,\n" +
                    "            \"name\":\"drakeet\"\n" +
                    "        }\n" +
                    "    }\n" +
                    "]";
}
