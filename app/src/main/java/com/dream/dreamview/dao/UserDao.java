package com.dream.dreamview.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

/**
 * Created by Administrator on 2017/8/15.
 */
@Dao
public interface UserDao {

    @Insert
    void insert(User user);
}
