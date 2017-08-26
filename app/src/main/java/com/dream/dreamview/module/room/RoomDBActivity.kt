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
import com.dream.dreamview.util.AssetsHelper
import com.dream.dreamview.util.FileHelper
import com.dream.dreamview.util.LogUtil
import com.dream.dreamview.util.ToastUtil
import com.hpu.baserecyclerviewadapter.BaseRecyclerViewAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created on 2017/8/22.
 */
class RoomDBActivity : NavBaseActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BaseRecyclerViewAdapter<RoomItem>
    private val mDisposable = CompositeDisposable()
    private val DB_NAME = "test-db"

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
        recyclerView = findViewById(R.id.recycler_view)
        adapter = BaseRecyclerViewAdapter()
        recyclerView.adapter = adapter
        val list = ArrayList<RoomItem>()
        list.add(RoomItem("插入"))
        list.add(RoomItem("QueryByFlowable"))
        list.add(RoomItem("QueryByMaybe"))
        list.add(RoomItem("QueryBySingle"))
        list.add(RoomItem("更新"))
        list.add(RoomItem("删除"))
        list.add(RoomItem("copyFromAssets"))
        list.add(RoomItem("copyToSD"))
        adapter.data = list
        adapter.setOnItemClickListener { view, position ->
            action(position)
        }
    }

    private fun action(position: Int) {
        when (position) {
            0 -> insert()
            1 -> getUserByFlowable("haha_100")
            2 -> getUserByMaybe("haha_100")
            3 -> getUserBySingle("haha_100")
            4 -> {
                val user = User()
                user.userName = "jjjj"
                user.userId = "user_id_0"
                updateUser(user)
            }
            5 -> deleteUser()
            6 -> copyDBFromAssets()
            7 -> copyDBToSD()
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
        mDisposable.add(UserModel.getInstance().insertUser(users).subscribe({ ToastUtil.showShortToast(this, "database success") }) { throwable ->
            ToastUtil.showShortToast(this, "database fail")
            LogUtil.e(throwable.message)
        })
    }

    private fun getUserByFlowable(userName: String) {
        // TODO 注意：！！！！
        //TODO In your Room’s table Dao, return Flowable<List<User>> that will stream data of specified Query each time database is updated.
        // TODO 每次数据库更新(插入、删除数据等操作)，都会通知这个返回为Flowable的查询方法
        mDisposable.add(UserModel.getInstance()
                .getUserByFlowable(userName)
                .subscribe({
                    // 查询成功才会执行onSuccess
                    ToastUtil.showShortToast(this, "flowable success")
                },
                        { ToastUtil.showShortToast(this, "flowable error") },
                        // onComplete 无论什么情况下都不会执行
                        { ToastUtil.showShortToast(this, "flowable complete") }))
    }

    private fun getUserByMaybe(userName: String) {
        mDisposable.add(UserModel.getInstance()
                .getUserByMayby(userName)
                .subscribe({ ToastUtil.showShortToast(this, "mayby success") },
                        { ToastUtil.showShortToast(this, "mayby error") },
                        { ToastUtil.showShortToast(this, "mayby complete") }))
    }

    private fun getUserBySingle(userName: String) {
        mDisposable.add(UserModel.getInstance()
                .getUserBySingle(userName)
                .subscribe({ ToastUtil.showShortToast(this, "single success") }) { throwable ->
                    if (throwable is EmptyResultSetException) {
                        ToastUtil.showShortToast(this, "single no data found")
                    } else {
                        ToastUtil.showShortToast(this, "single find error")
                    }
                })
    }

    private fun updateUser(user: User) {
        mDisposable.add(UserModel.getInstance().updateUser(user).subscribe(
                { ToastUtil.showShortToast(this, "更新成功") },
                { ToastUtil.showShortToast(this, "更新失败...") }))
    }

    private fun deleteUser() {
        mDisposable.add(UserModel.getInstance().deleteUser().subscribe())
    }

    private fun copyDBFromAssets() {
        // 复制assets目录下多个db文件
        AssetsHelper.copyAssetsDB(this, "db")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({  v ->
                    if (v == AssetsHelper.BACKUP_SUCCESS) {
                        ToastUtil.showLongToast(this, "备份成功")
                    } else if (v == AssetsHelper.FILE_BACKUP_EXISTS) {
                        ToastUtil.showLongToast(this, "备份文件已存在")
                    } },
                        { ToastUtil.showShortToast(this, "备份失败") })
        // 复制assets目录下单个db文件
        AssetsHelper.copyAssetsDB(this, DB_NAME, DB_NAME)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ v ->
                    if (v == AssetsHelper.BACKUP_SUCCESS) {
                        ToastUtil.showLongToast(this, "备份成功")
                    } else if (v == AssetsHelper.FILE_BACKUP_EXISTS) {
                        ToastUtil.showLongToast(this, "备份文件已存在")
                    }
                },
                        { ToastUtil.showShortToast(this, "备份失败") })
    }

    private fun copyDBToSD() {
        mDisposable.add(FileHelper
                .copyDbToExternalStorage(applicationContext, "test-db", "ttt")
                .retry(1) // 失败重复次数(这里是重复一次)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ v ->
                    if (v == FileHelper.BACKUP_SUCCESS) {
                        ToastUtil.showShortToast(applicationContext, "复制SD卡成功...")
                    } else if (v == FileHelper.UNUSUAL_STATUS) {
                        ToastUtil.showShortToast(applicationContext, "存储卡不可用")
                    } else if (v == FileHelper.FILE_BACKUP_EXISTS) {
                        ToastUtil.showShortToast(applicationContext, "备份文件已存在")
                    } else if (v == FileHelper.FILE_SOURCE_NOT_EXIST) {
                        ToastUtil.showShortToast(applicationContext, "源文件不存在")
                    }
                }, {
                    ToastUtil.showShortToast(applicationContext, "复制SD卡失败...")
                }))
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposable.clear()
    }
}