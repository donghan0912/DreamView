package com.dream.dreamview.test;

import android.arch.persistence.room.EmptyResultSetException;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dream.dreamview.R;
import com.dream.dreamview.base.NavBaseActivity;
import com.dream.dreamview.dao.User;
import com.dream.dreamview.dao.UserModel;
import com.dream.dreamview.util.LogUtil;
import com.dream.dreamview.util.ToastUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

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
                exportDatabse("test-db", "ttt");
                circleProgress.setProgress(50);

                mDisposable.add(UserModel.getInstance().getUser().subscribe(new Consumer<List<User>>() {
                    @Override
                    public void accept(List<User> users) throws Exception {
                        ToastUtil.showShortToast(getApplicationContext(), "查询有" + users.size() + "data");
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

        /*List<User> users = new ArrayList<>();
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
        }));*/

        try {
            copyDBToDatabases(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    // TODO 不完善，copy文件，最好放到子线程中
    public void exportDatabse(String databaseName, String copyDbName) {
        boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (!isSDPresent) {
            // sd card not avalilable
            return;
        }
        try {
            File data = Environment.getDataDirectory();
            File file = new File(Environment.getExternalStorageDirectory(), getPackageName());
            if (!file.exists()) { // 判断文件夹是否存在
                if (!file.mkdirs()) { // 判断文件夹是否创建成功
                    // 文件夹创建失败，直接使用sd根目录
                    file = Environment.getExternalStorageDirectory();
                }
            }
            if (file.canWrite()) {
                String currentDBPath = "//data//" + getPackageName() + "//databases//" + databaseName + "";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(file, copyDbName);
                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // clear all the subscriptions
        mDisposable.clear();
    }

    private static final String DB_PATH = "/data/data/com.dream.dreamview/databases/";
    private static final String DB_NAME = "test-db";

    /**
     * 复制assets下数据库到data/data/packagename/databases
     * @param context
     * @throws
     */
    // TODO 不完善，copy文件，最好放到子线程中
    public void copyDBToDatabases(Context context) throws IOException {
        String outFileName = DB_PATH + DB_NAME;

        File file = new File(DB_PATH);
        if (!file.mkdirs()) {
            file.mkdirs();
        }

        if (new File(outFileName).exists()) {
            // 数据库已经存在，无需复制
            return;
        }

        InputStream myInput = context.getAssets().open(DB_NAME);
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }
}
