package com.dream.dreamview.module.room

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.dream.dreamview.R
import com.dream.dreamview.base.NavBaseActivity
import com.dream.dreamview.module.room.adapter.RoomItem
import com.dream.dreamview.util.ToastUtil
import com.hpu.baserecyclerviewadapter.BaseRecyclerViewAdapter

/**
 * Created on 2017/8/22.
 */
class RoomDBActivity : NavBaseActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BaseRecyclerViewAdapter<RoomItem>

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, RoomDBActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getContentView(): Int {
        return R.layout.room_roomdb_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = BaseRecyclerViewAdapter()
        recyclerView.adapter = adapter
        val list = ArrayList<RoomItem>()
        list.add(RoomItem("数据库"))
        list.add(RoomItem("数据库"))
        list.add(RoomItem("数据库"))
        list.add(RoomItem("数据库"))
        list.add(RoomItem("数据库"))
        list.add(RoomItem("数据库"))
        adapter.data = list
        adapter.setOnItemClickListener { p0, p1 ->
            ToastUtil.showShortToast(applicationContext, "tt")
        }
    }
}