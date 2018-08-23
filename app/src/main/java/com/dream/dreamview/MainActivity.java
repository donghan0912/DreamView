package com.dream.dreamview;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dream.dreamview.base.NavBaseActivity;
import com.dream.dreamview.module.anim.AnimationActivity;
import com.dream.dreamview.module.anim.CustomRefreshActivity;
import com.dream.dreamview.module.anim.LottieActivity;
import com.dream.dreamview.module.common.DatePickerActivity;
import com.dream.dreamview.module.common.TabActivity;
import com.dream.dreamview.module.flutter.FlutterDemoActivity;
import com.dream.dreamview.module.meinv.BeautyActivity;
import com.dream.dreamview.module.multi.MultiTypeActivity;
import com.dream.dreamview.module.multistatus.MultiStatusActivity;
import com.dream.dreamview.module.room.RoomDBActivity;
import com.dream.dreamview.module.rxbus.RxBus;
import com.dream.dreamview.module.secret.SecretActivity;
import com.dream.dreamview.module.video.VideoActivity;
import com.dream.dreamview.module.video.VideoListActivity;
import com.dream.dreamview.module.web.TencentWebActivity;
import com.dream.dreamview.module.web.WebActivity;
import com.dream.dreamview.sample.CustomNavFoldSampleActivity;
import com.dream.dreamview.sample.CustomeToolbarSampleActivity;
import com.dream.dreamview.sample.NavFoldSampleActivity;
import com.dream.dreamview.test.KotlinActivity;
import com.dream.dreamview.test.RetrofitActivity;
import com.dream.dreamview.test.TestActivity;
import com.dream.dreamview.util.ToastUtil;
import com.hpu.baserecyclerviewadapter.BaseItem;
import com.hpu.baserecyclerviewadapter.BaseRecyclerViewAdapter;
import com.hpu.baserecyclerviewadapter.BaseViewHolder;

import butterknife.BindArray;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends NavBaseActivity implements View.OnClickListener {

    @BindArray(R.array.main_card)
    String[] mData;

    private Disposable subscribe;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        closeSwipe();
        setTitle("首页");
        setDisplayHomeAsUpEnabled(true);
        subscribe = RxBus.getInstance().toObservable().subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                ToastUtil.showShortToast(MainActivity.this, String.valueOf(o));
            }
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        BaseRecyclerViewAdapter<MainItem> adapter = new BaseRecyclerViewAdapter<>();
        for (String data : mData) {
            MainItem mainItem = new MainItem(data);
            adapter.addData(mainItem);
        }
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                if (i == 0) {
                    MultiStatusActivity.start(MainActivity.this);
                } else if (i == 1) {
                    startActivity(new Intent(MainActivity.this, NavFoldSampleActivity.class));
                } else if (i == 2) {
                    startActivity(new Intent(MainActivity.this, CustomNavFoldSampleActivity.class));
                } else if (i == 3) {
                    startActivity(new Intent(MainActivity.this, SwipActivity.class));
                } else if (i == 4) {
                    startActivity(new Intent(MainActivity.this, CustomeToolbarSampleActivity.class));
                } else if (i == 5) {
                    startActivity(new Intent(MainActivity.this, RetrofitActivity.class));
                } else if (i == 6) {
                    startActivity(new Intent(MainActivity.this, BeautyActivity.class));
                } else if (i == 7) {
                    startActivity(new Intent(MainActivity.this, MultiTypeActivity.class));
                } else if (i == 8) {
                    startActivity(new Intent(MainActivity.this, VideoActivity.class));
                } else if (i == 9) {
                    startActivity(new Intent(MainActivity.this, VideoListActivity.class));
                } else if (i == 10) {
                    startActivity(new Intent(MainActivity.this, AnimationActivity.class));
                } else if (i == 11) {
                    startActivity(new Intent(MainActivity.this, TestActivity.class));
                } else if (i == 12) {
                    startActivity(new Intent(MainActivity.this, KotlinActivity.class));
                } else if (i == 13) {
                    RoomDBActivity.start(MainActivity.this);
                } else if (i == 14) {
                    SecretActivity.start(MainActivity.this);
                } else if (i == 15) {
                    WebActivity.start(MainActivity.this);
                } else if (i == 16) {
                    TencentWebActivity.start(MainActivity.this);
                } else if (i == 17) {
                    LottieActivity.start(MainActivity.this);
                } else if (i == 18) {
                    CustomRefreshActivity.start(MainActivity.this);
                } else if (i == 19) {
                    DatePickerActivity.start(MainActivity.this);
                } else if (i == 20) {
                    TabActivity.start(MainActivity.this);
                } else if (i == 21) {
                    FlutterDemoActivity.start(MainActivity.this);
                }
            }
        });
    }

    class MainItem extends BaseItem<String> {

        MainItem(String s) {
            this.mData = s;
        }

        @Override
        public int getLayoutResource() {
            return R.layout.main_item;
        }

        @Override
        public void onBindViewHolder(BaseViewHolder baseViewHolder, int i) {
            baseViewHolder.setText(R.id.text, mData);
        }
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ToastUtil.showShortToast(this, "返回");
                break;
            case R.id.setting:
                ToastUtil.showShortToast(this, "设置");
                break;
            case R.id.about:
                ToastUtil.showShortToast(this, "关于");
                break;
            case R.id.contact:
                ToastUtil.showShortToast(this, "联系");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (subscribe != null) {
            subscribe.dispose();
        }
        super.onDestroy();
    }
}
