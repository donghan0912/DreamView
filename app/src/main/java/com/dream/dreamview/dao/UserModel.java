package com.dream.dreamview.dao;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.internal.operators.completable.CompletableFromAction;

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

    // TODO not work, just mark it
    public Completable updateUserName(final User user) {
        return new CompletableFromAction(new Action() {
            @Override
            public void run() throws Exception {
                mUserDao.insert(user);
            }
        });
    }

}
