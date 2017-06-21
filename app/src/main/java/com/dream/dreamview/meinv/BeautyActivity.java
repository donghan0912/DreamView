package com.dream.dreamview.meinv;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dream.dreamview.R;
import com.dream.dreamview.base.NavBaseActivity;
import com.hpu.baserecyclerviewadapter.BaseRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/6/21.
 */

public class BeautyActivity extends NavBaseActivity{


    @Override
    protected int getContentView() {
        return R.layout.meinv_activity_beauty;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List list = new ArrayList();
        for(int i = 0; i<20; i++) {
            list.add(new ImgItem());
        }
        BaseRecyclerViewAdapter adapter = new BaseRecyclerViewAdapter(list);

        recyclerView.setAdapter(adapter);
    }
}
