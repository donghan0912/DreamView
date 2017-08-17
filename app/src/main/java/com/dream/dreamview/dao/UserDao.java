package com.dream.dreamview.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Administrator on 2017/8/15
 */
@Dao
public interface UserDao {

    /**
     * Insert a user in the database. If the user already exists, replace it.
     *
     * @param user the user to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<User> users);

        @Query("SELECT * FROM user_table_1")
        List<User> getUser();

//    @Query("SELECT * FROM Users")
//    Flowable<User> getUser();
//
//    @Query("DELETE FROM user_table_1")
//    void deleteAllUsers();
}
