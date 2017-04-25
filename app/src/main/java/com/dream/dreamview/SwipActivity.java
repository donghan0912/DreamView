package com.dream.dreamview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lenovo on 2017/4/25.
 */

public class SwipActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swip);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerAdapter adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper callback = new ItemTouchHelper(new ItemTouchHelpCallback(adapter));
        callback.attachToRecyclerView(recyclerView);
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> implements ItemTouchHelpCallback.ItemHelpCallback {

        private final String[] STRINGS = new String[]{
                "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
                "Ten", "Ten", "Ten", "Ten"
        };

        private final List<String> mItems = new ArrayList<>();

        public RecyclerAdapter() {
            mItems.addAll(Arrays.asList(STRINGS));
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            return itemViewHolder;
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            holder.textView.setText(mItems.get(position));
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        @Override
        public void onItemDismiss(int position) {
            mItems.remove(position);
            notifyItemRemoved(position);
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder{

            public final TextView textView;

            public ItemViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView;
            }
        }
    }
}
