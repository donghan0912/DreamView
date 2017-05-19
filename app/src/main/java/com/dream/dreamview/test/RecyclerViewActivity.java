package com.dream.dreamview.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dream.dreamview.R;
import com.dream.dreamview.base.BaseRecyclerViewAdapter;
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
        BaseRecyclerViewAdapter adapter = new BaseRecyclerViewAdapter() {

            private static final int TYPE_FIR = 1;
            private static final int TYPE_SEC = 2;
            private static final int TYPE_THR = 3;

            @Override
            public int getLayoutId(int viewType) {
                if (viewType == TYPE_FIR) {
                    return R.layout.test_activity_recyclerview_item;
                } else if (viewType == TYPE_SEC){
                    return R.layout.test_activity_recyclerview_item_two;
                } else {
                    return R.layout.test_activity_recyclerview_item_three;
                }
            }

            @Override
            public int getItemViewType(int position) {
                if (position == 17) {
                    return TYPE_THR;
                }
                if (position % 2 == 0) {
                    return TYPE_FIR;
                } else {
                    return TYPE_SEC;
                }
            }

            @Override
            public void onBindRecyclerViewHolder(BaseViewHolder holder, int position) {
//                holder.setText(R.id.content, (String)getItem(position))
//                      .setText(R.id.content2, getItem(position) + "sss");
            }
        };
//        recyclerView.setAdapter(adapter);
        adapter.setData(list);
        List<BaseAdapterItem> mData = new ArrayList<>();
        for (int i = 0; i <10; i++) {
            mData.add(new TextItem());
            if (i == 9) {
                mData.add(new TwoTextItem());
            }
        }
        BaseAdapter textItemBaseAdapter = new BaseAdapter(mData);
        recyclerView.setAdapter(textItemBaseAdapter);
    }

}
