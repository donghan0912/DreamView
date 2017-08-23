package com.dream.dreamview.module.room.adapter

import com.dream.dreamview.R
import com.hpu.baserecyclerviewadapter.BaseItem
import com.hpu.baserecyclerviewadapter.BaseViewHolder

/**
 * Created on 2017/8/23.
 */
class RoomItem(t: String) : BaseItem<String>(t) {

    override fun getLayoutResource(): Int {
        return R.layout.room_room_activity_item
    }

    override fun onBindViewHolder(p0: BaseViewHolder?, p1: Int) {
        p0?.setText(R.id.text, data)
    }

}