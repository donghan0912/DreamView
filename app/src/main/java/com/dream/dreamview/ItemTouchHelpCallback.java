package com.dream.dreamview;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by lenovo on 2017/4/25.
 */

public class ItemTouchHelpCallback extends ItemTouchHelper.Callback {

    private ItemHelpCallback callback;

    public ItemTouchHelpCallback(ItemHelpCallback callback) {
        this.callback = callback;
    }
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        callback.onItemDismiss(viewHolder.getAdapterPosition());
    }

    interface ItemHelpCallback {
//        void onItemMove(int fromPosition, int toPosition);

        void onItemDismiss(int position);
    }
}
