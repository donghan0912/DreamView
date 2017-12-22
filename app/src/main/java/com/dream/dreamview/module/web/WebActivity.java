package com.dream.dreamview.module.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.dream.dreamview.R;
import com.dream.dreamview.base.BaseActivity;
import com.dream.dreamview.util.LogUtil;
import com.dream.dreamview.util.ToastUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/12/18.
 */

public class WebActivity extends BaseActivity implements View.OnTouchListener{

    private WebView webview;

    public static void start(Context context) {
        context.startActivity(new Intent(context, WebActivity.class));
    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity_web);
        webview = findViewById(R.id.web_view);
//        webview.loadUrl("http://www.haorooms.com/uploads/example/execCommand/demo4.html");
//        webview.loadUrl("file:///android_asset/demo.html");
//        webview.loadUrl("http://www.jianshu.com/p/6f55b90032ce");
//        webview.loadUrl("file:///android_asset/test.html");
        webview.loadUrl("file:///android_asset/t.html");
        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webview.loadUrl("javascript:ColorizeSelection ('#FF0000')");
            }
        });

        // 1. startContainer 选中区第一个节点的父节点
        // 2. startOffset 起点偏移量
        // TODO js 选择器
        // TODO 通过以下步骤可以拿到选中区域的id
        // TODO var startContainer1 = range1.startContainer;
        // TODO var startContainer2 = range2.startContainer;
        // TODO var id1 = startContainer1.parentElement.id;
        // TODO var id2 = startContainer2.parentElement.id;

        // TODO range.isPointInRange 判断是否点中选中区域(可以用来弹出删除菜单按钮)


        webview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
//                webview.loadUrl("javascript:longPress ()");

                webview.loadUrl("javascript:createRangeTextt()");
                webview.loadUrl("javascript:ColorizeSelection ('#FF0000')");
                ToastUtil.showShortToast(WebActivity.this, "菜单该出来了");

                return true;
            }
        });
        webview.setOnTouchListener(this);
        webview.addJavascriptInterface(WebActivity.this, "android");

        final CustomActionWebView mCustomActionWebView = findViewById(R.id.web_view_2);
        List<String> list = new ArrayList<>();
        list.add("复制");
        list.add("搜索");
        list.add("标记");
//        mCustomActionWebView.setWebViewClient(new CustomWebViewClient());

        //设置item
        mCustomActionWebView.setActionList(list);

        //链接js注入接口，使能选中返回数据
        mCustomActionWebView.linkJSInterface();

        mCustomActionWebView.getSettings().setBuiltInZoomControls(true);
        mCustomActionWebView.getSettings().setDisplayZoomControls(false);
        //使用javascript
        mCustomActionWebView.getSettings().setJavaScriptEnabled(true);
        mCustomActionWebView.getSettings().setDomStorageEnabled(true);


        //增加点击回调
        mCustomActionWebView.setActionSelectListener(new ActionSelectListener() {
            @Override
            public void onClick(String title, String selectText) {
                if(title.equals("APIWeb")) {


                    return;
                }
                Toast.makeText(WebActivity.this, "Click Item: " + title + "。\n\nValue: " + selectText, Toast.LENGTH_LONG).show();
            }
        });

        //加载url
        mCustomActionWebView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCustomActionWebView.loadUrl("http://www.jianshu.com/p/6f55b90032ce");
            }
        }, 1000);



        registerForContextMenu(webview);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.web_view_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.remark:
                try {
                    WebActivity.class.getMethod("emulateShiftHeld").invoke(this);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                ToastUtil.showShortToast(this, "标记");
                return true;
            case R.id.delete:
                try {
                    WebActivity.class.getMethod("selectText").invoke(this);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                ToastUtil.showShortToast(this, "删除");
                return true;
            default:
                super.onContextItemSelected(item);
        }
        return false;
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        float xPoint = getDensityIndependentValue(event.getX(), this) / getDensityIndependentValue(webview.getScale(), this);
        float yPoint = getDensityIndependentValue(event.getY(), this) / getDensityIndependentValue(webview.getScale(), this);
        LogUtil.e(xPoint + "/" + yPoint + " ####################");
        webview.loadUrl("javascript:createRangeTextt(xPoint, yPoint)");
        return false;
    }

    public float getDensityDependentValue(float val, Context ctx){

        // Get display from context
        Display display = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        // Calculate min bound based on metrics
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        return val * (metrics.densityDpi / 160f);

        //return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, val, metrics);

    }

    /**
     * Returns the density independent value of the given float
     * @param val
     * @param ctx
     * @return
     */
    public float getDensityIndependentValue(float val, Context ctx){

        // Get display from context
        Display display = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        // Calculate min bound based on metrics
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);


        return val / (metrics.densityDpi / 160f);

        //return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, val, metrics);

    }
    private String startId;
    private String startOffset;
    private String endId;
    private String endOffset;
    @JavascriptInterface
    public void startFunction(final String startId, final String startOffset, final String endId, final String endOffset){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WebActivity.this.startId = startId;
                WebActivity.this.startOffset = startOffset;
                WebActivity.this.endId = endId;
                WebActivity.this.endOffset = endOffset;
                ToastUtil.showShortToast(WebActivity.this,
                        startId + "==" + startOffset + "==" + endId + "==" + endOffset);
            }
        });
    }

    @JavascriptInterface
    public void startFunction(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO 调转测试
                SyncWebActivity.start(WebActivity.this, startId, startOffset, endId, endOffset);
            }
        });
    }
}
