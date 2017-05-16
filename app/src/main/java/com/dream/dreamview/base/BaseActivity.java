package com.dream.dreamview.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.dream.dreamview.R;

/**
 * Created by lenovo on 2017/4/18.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 由于在them中配置starbar 为透明样式，所以在基类中设置starbar颜色
        // 注：未检查是否影响子类，若影响，可在具体子类中单独设置starbar颜色
        // 实际项目中，可不设置starbar颜色，因为大部分都带有toolbar
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }*/
    }
}
