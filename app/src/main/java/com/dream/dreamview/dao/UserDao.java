package com.dream.dreamview.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

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
    void insertOrUpdate(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(List<User> users);

    @Query("SELECT * FROM user_table_1")
    Flowable<List<User>> getUser();

    @Query("select * from user_table_1 where userName = 'sss'")
    Flowable<User> getUsre();

    @Query("select * from user_table_1 where userName = 'haha_d100'")
    Maybe<User> getUsrByMaybe();

    @Query("select * from user_table_1 where userName = :userName")
    Single<User> getUsressBySingle(String userName);

//    @Query("SELECT * FROM Users")
//    Flowable<User> getUser();
//
//    @Query("DELETE FROM test-db")
//    void deleteAllUsers();
}
