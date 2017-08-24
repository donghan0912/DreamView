package com.dream.dreamview.module.room.dao;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Administrator on 2017/8/15
 */
@Entity(tableName = "user_room")
public class User {
    @PrimaryKey
    public String userId;
    public String userName;
    public String password;
}
