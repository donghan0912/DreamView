package com.dream.dreamview.test;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dream.dreamview.R;
import com.dream.dreamview.base.BaseActivity;

import java.util.zip.Inflater;

/**
 * Created by lenovo on 2017/5/14.
 */

public class RecyclerViewActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        setContentView(R.layout.test_activity_recyclerview);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new TestAdapter());
    }

    public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {


        @Override
        public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View item = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.test_activity_recyclerview_item, parent, false);
            return new TestViewHolder(item);
        }

        @Override
        public void onBindViewHolder(TestViewHolder holder, int position) {
            holder.content.setText("text");
        }

        @Override
        public int getItemCount() {
            return 20;
        }

        public class TestViewHolder extends RecyclerView.ViewHolder {
            private TextView content;

            public TestViewHolder(View itemView) {
                super(itemView);
                content = (TextView) itemView.findViewById(R.id.content);
            }
        }
    }
}
