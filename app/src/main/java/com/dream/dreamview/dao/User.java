package com.dream.dreamview.dao;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Administrator on 2017/8/15
 */
@Entity(tableName = "user_table_1")
public class User {
    @PrimaryKey
    public String userId;
    public String userName;
    public String password;
}
