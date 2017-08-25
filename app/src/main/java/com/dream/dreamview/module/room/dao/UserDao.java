package com.dream.dreamview.module.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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
    void insert(User user);

    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    void insertUser(List<User> users);

    @Update
    void update(User user);

    @Query("SELECT * FROM user_room")
    Flowable<List<User>> getUser();

    @Query("select * from user_room where userName = :userName")
    Flowable<User> getUsre(String userName);

    @Query("select * from user_room where userName = :userName")
    Maybe<User> getUsrByMaybe(String userName);

    @Query("select * from user_room where userName = :userName")
    Single<User> getUsressBySingle(String userName);

    @Query("DELETE FROM user_room")
    void deleteUser();
}
