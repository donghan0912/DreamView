package com.dream.dreamview.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dream.dreamview.R;
import com.dream.dreamview.base.BaseAdapter;
import com.dream.dreamview.base.BaseViewHolder;
import com.dream.dreamview.base.NavBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 实现功能：添加、删除、拖拽、点击、长按点击、滑动删除、添加head、footer
 * Created by lenovo on 2017/5/14.
 */

public class RecyclerViewActivity extends NavBaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.test_activity_recyclerview;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("RecyclerView");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        final List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("第" + i + "数据");
        }
        recyclerView.setAdapter(new BaseAdapter<String>(R.layout.test_activity_recyclerview_item, list) {
            @Override
            public void onBindRecyclerViewHolder(BaseViewHolder holder, int position) {
                holder.setText(R.id.content, list.get(position));
            }
        });
    }

}
