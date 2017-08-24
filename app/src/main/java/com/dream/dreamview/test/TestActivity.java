package com.dream.dreamview.test;

import android.arch.persistence.room.EmptyResultSetException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dream.dreamview.R;
import com.dream.dreamview.base.NavBaseActivity;
import com.dream.dreamview.module.room.dao.User;
import com.dream.dreamview.module.room.dao.UserModel;
import com.dream.dreamview.util.AssetsHelper;
import com.dream.dreamview.util.FileHelper;
import com.dream.dreamview.util.LogUtil;
import com.dream.dreamview.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo on 2017/7/21
 */

public class TestActivity extends NavBaseActivity {

    private ViewPager viewPager;
    private Button btn;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected int getContentView() {
        return R.layout.ttt;
//        return R.layout.activity_test;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.ttt);
//        closeSwipe();
        viewPager = findViewById(R.id.view_pager);
        btn = findViewById(R.id.btn);
        final CircleProgress circleProgress = findViewById(R.id.circle);
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDisposable.add(FileHelper
                        .copyDbToExternalStorage(getApplicationContext(), "test-db", "ttt")
                        .retry(1) // 失败重复次数(这里是重复一次)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                LogUtil.e("======================1111111");
                                ToastUtil.showShortToast(getApplicationContext(), "复制SD卡成功！！！");
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                LogUtil.e("======================222222222222222");
                                ToastUtil.showShortToast(getApplicationContext(), "复制SD卡失败！！！");
                            }
                        }));

                circleProgress.setProgress(50);

                List<User> users = new ArrayList<>();
                for (int i = 0; i < 20000; i++) {
                    User user = new User();
                    user.userName = "haha_" + i;
                    user.userId = "user_id_" + i;
                    user.password = null;
                    users.add(user);
                }
                mDisposable.add(UserModel.getInstance().updateUserName(users).subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        ToastUtil.showShortToast(TestActivity.this, "database success");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtil.showShortToast(TestActivity.this, "database fail");
                        LogUtil.e(throwable.getMessage());
                    }
                }));

                mDisposable.add(UserModel.getInstance().getUserName().subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        ToastUtil.showShortToast(getApplicationContext(), "查询有" + s + "  data");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtil.showShortToast(getApplicationContext(), "cha xun fail");
                    }
                }));

                UserModel.getInstance().getUserNameByMayby().subscribe(new MaybeObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        LogUtil.e("=============mayby success");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        LogUtil.e("===============mayby error");
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e("=========mayby complete");
                    }
                });

                mDisposable.add(UserModel.getInstance().getUserNameByMayby().subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        LogUtil.e("mayby success");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e("mayby error");
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtil.e("mayby complete");
                    }
                }));


                mDisposable.add(UserModel.getInstance().getUserBySingle("haha_100").subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        LogUtil.e("single success");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (throwable instanceof EmptyResultSetException) {
                            LogUtil.e("single no data found");
                        } else {
                            LogUtil.e("single find error");
                        }
                    }
                }));

            }
        });

        final List<View> list = new ArrayList<>();
        View view1 = getLayoutInflater().inflate(R.layout.item_view_pager_fir, null);
        View view2 = getLayoutInflater().inflate(R.layout.item_view_pager_sec, null);
        View view3 = getLayoutInflater().inflate(R.layout.item_view_pager_third, null);
        list.add(view1);
        list.add(view2);
        list.add(view3);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(list.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(list.get(position));
                return list.get(position);
            }
        });

        // TODO http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2017/0728/8278.html
        // https://medium.com/google-developers/room-rxjava-acb0cd4f3757
        // https://github.com/googlesamples/android-architecture-components/blob/master/BasicRxJavaSample/app/src/main/java/com/example/android/observability/ui/UserActivity.java


        // 复制assets目录下多个db文件
        AssetsHelper.copyAssetsDB(this, "db")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                LogUtil.e("复制成功11111111111");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                LogUtil.e("复制失败11111111");
            }
        });
        // 复制assets目录下单个db文件
        AssetsHelper.copyAssetsDB(this, DB_NAME, DB_NAME)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                LogUtil.e("复制成功2222222");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                LogUtil.e("复制失败222222222");
            }
        });
        mDisposable.add(UserModel.getInstance().getUser().subscribe(new Consumer<List<User>>() {
            @Override
            public void accept(List<User> users) throws Exception {
                ToastUtil.showShortToast(getApplicationContext(), "查询有" + users.size() + "data");
            }
        }));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int[] location = new int[2];
        viewPager.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
        int x = location[0];
        int y = location[1];
        int height = viewPager.getHeight();
        int width = viewPager.getWidth();
        this.mSwipeBackLayout.setUnInterceptPos(x, y, x + width, y + height);

        int[] location2 = new int[2];
        btn.getLocationInWindow(location2); //获取在当前窗口内的绝对坐标
        int x2 = location2[0];
        int y2 = location2[1];
        int height2 = btn.getHeight();
        int width2 = btn.getWidth();
        this.mSwipeBackLayout.setUnInterceptPos(x2, y2, x2 + width2, y2 + height2);
    }



    @Override
    protected void onPause() {
        super.onPause();
        // clear all the subscriptions
        mDisposable.clear();
    }

    private static final String DB_PATH = "/data/data/com.dream.dreamview/databases/";
    private static final String DB_NAME = "test-db";
}
