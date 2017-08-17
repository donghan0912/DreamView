package com.dream.dreamview.dao;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.internal.operators.completable.CompletableFromAction;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2017/8/17.
 */

public class UserModel {
    UserDao mUserDao;
    private UserModel() {
        this.mUserDao = AppDatabase.getInstance().userDao();
    }

    private static class UserLoader {
        private static final UserModel INSTANCE = new UserModel();
    }

    public static UserModel getInstance() {
        return UserLoader.INSTANCE;
    }

    public Completable updateUserName(final List<User> user) {
        return new CompletableFromAction(new Action() {
            @Override
            public void run() throws Exception {
                mUserDao.insert(user);
            }
        }).subscribeOn(Schedulers.io());
    }

}
