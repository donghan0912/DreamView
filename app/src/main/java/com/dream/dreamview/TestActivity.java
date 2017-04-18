package com.dream.dreamview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/4/17.
 */

public class TestActivity extends TTTNavBaseActivity {


    @Override
    protected int getContentView() {
        return R.layout.activity_test;
    }

    @Override
    protected int getToolbarView() {
        return R.layout.base_toolbar_test;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setDisplayHomeAsUpEnabled(false);
        setTitle("这是自定义标题madf");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "back", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
