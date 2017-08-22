package com.dream.dreamview.module.room

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.dream.dreamview.R
import com.dream.dreamview.base.NavBaseActivity

/**
 * Created on 2017/8/22.
 */
class RoomDBActivity : NavBaseActivity() {

    companion object {
        @JvmStatic
        fun start(context : Context) {
            val intent = Intent(context, RoomDBActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getContentView(): Int {
        return R.layout.room_roomdb_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}