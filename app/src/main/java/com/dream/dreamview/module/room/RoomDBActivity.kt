package com.dream.dreamview.module.room

import android.arch.persistence.room.EmptyResultSetException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.dream.dreamview.R
import com.dream.dreamview.base.NavBaseActivity
import com.dream.dreamview.module.room.adapter.RoomItem
import com.dream.dreamview.module.room.dao.User
import com.dream.dreamview.module.room.dao.UserModel
import com.dream.dreamview.util.LogUtil
import com.dream.dreamview.util.ToastUtil
import com.hpu.baserecyclerviewadapter.BaseRecyclerViewAdapter
import io.reactivex.disposables.CompositeDisposable

/**
 * Created on 2017/8/22.
 */
class RoomDBActivity : NavBaseActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BaseRecyclerViewAdapter<RoomItem>
    private val mDisposable = CompositeDisposable()

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
        title = "数据库"
        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        adapter = BaseRecyclerViewAdapter()
        recyclerView.adapter = adapter
        val list = ArrayList<RoomItem>()
        list.add(RoomItem("插入"))
        list.add(RoomItem("查询"))
        list.add(RoomItem("删除"))
        list.add(RoomItem("升级"))
        list.add(RoomItem("copyFromAssets"))
        list.add(RoomItem("copyToSD"))
        adapter.data = list
        adapter.setOnItemClickListener { view, position ->
            action(position)
        }
    }

    private fun action(position: Int) {
        when(position) {
            0 -> insert()
            1 -> getUsers()
            2 -> getUser()
        }
    }

    private fun insert() {
        // TODO http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2017/0728/8278.html
        // https://medium.com/google-developers/room-rxjava-acb0cd4f3757
        // https://github.com/googlesamples/android-architecture-components/blob/master/BasicRxJavaSample/app/src/main/java/com/example/android/observability/ui/UserActivity.java
        val users = ArrayList<User>()
        for (i in 0..200) {
            val user = User()
            user.userName = "haha_" + i
            user.userId = "user_id_" + i
            user.password = null
            users.add(user)
        }
        mDisposable.add(UserModel.getInstance().updateUserName(users).subscribe({ ToastUtil.showShortToast(this, "database success") }) { throwable ->
            ToastUtil.showShortToast(this, "database fail")
            LogUtil.e(throwable.message)
        })
    }

    private fun getUsers() {
        // TODO 注意：！！！！
        //TODO In your Room’s table Dao, return Flowable<List<User>> that will stream data of specified Query each time database is updated.
        // TODO 每次数据库更新(插入、删除数据等操作)，都会通知这个返回为Flowable的查询方法
//        mDisposable.add(UserModel.getInstance().user.subscribe { users -> ToastUtil.showShortToast(this, "查询有" + users.size + "data") })

        mDisposable.add(UserModel.getInstance().getUserBySingle("haha_100").subscribe({ ToastUtil.showShortToast(this, "single success") }) { throwable ->
            if (throwable is EmptyResultSetException) {
                ToastUtil.showShortToast(this, "single no data found")
            } else {
                ToastUtil.showShortToast(this,"single find error")
            }
        })
    }

    private fun getUser() {

    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposable.clear()
    }
}