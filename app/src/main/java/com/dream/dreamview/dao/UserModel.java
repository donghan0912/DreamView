package com.dream.dreamview.dao;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.SchedulerSupport;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
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
                mUserDao.insertOrUpdate(user);
            }
        }).subscribeOn(Schedulers.io());
    }

    public Flowable<List<User>> getUser() {
        return mUserDao.getUser().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<String> getUserName() {
        return mUserDao.getUsre().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map(new Function<User, String>() {
            @Override
            public String apply(User user) throws Exception {
                return user.userName;
            }
        });
    }

    public Maybe<String> getUserNameByMayby() {
        return mUserDao.getUsrByMaybe()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<User, String>() {
                    @Override
                    public String apply(User user) throws Exception {
                        return user.userName;
                    }
                });
    }

    public Single<String> getUserBySingle(String userName) {
        return mUserDao.getUsressBySingle(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<User, String>() {
                    @Override
                    public String apply(User user) throws Exception {
                        return user.userName;
                    }
                });
    }

}
