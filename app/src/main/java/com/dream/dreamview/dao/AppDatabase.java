package com.dream.dreamview.dao;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;

import com.dream.dreamview.MyApplication;
import com.dream.dreamview.module.room.dao.User;
import com.dream.dreamview.module.room.dao.UserDao;

/**
 * Created by Administrator on 2017/8/15
 */
@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance() {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(MyApplication.getContext(), AppDatabase.class, "user_room")
                    .addMigrations(MIGRATION_1_2).build();
        }
        return INSTANCE;
    }

    // 数据库 schema 更改(比如：新增一个表、表中添加一个新字段等操作)，需要升级数据库时，用Migration
    // Migration(1, 2) 表示 从version1 更新到 version2
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase supportSQLiteDatabase) {

        }
    };

}
