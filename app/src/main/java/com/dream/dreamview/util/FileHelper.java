package com.dream.dreamview.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * Created on 2017/8/21
 */

public class FileHelper {

    private FileHelper() {

    }

    public static Single<Object> copyDbToExternalStorage(final Context context, final String databaseName, final String copyDbName) {
        return Single.create(new SingleOnSubscribe<Object>() {
            @Override
            public void subscribe(SingleEmitter<Object> singleEmitter) throws Exception {
                copyDb(context, databaseName, copyDbName, singleEmitter);
            }
        });
    }
    private static void copyDb(Context context, String databaseName, String copyDbName, SingleEmitter<Object> singleEmitter) {
        boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (!isSDPresent) {
            // sd card not avalilable
            try {
                singleEmitter.onError(new RuntimeException("SD卡不可用！"));
            } catch (Exception e) {
                singleEmitter.onError(e);
            }
            return;
        }
        try {
            File data = Environment.getDataDirectory();
            File file = new File(Environment.getExternalStorageDirectory(), context.getPackageName());
            if (!file.exists()) { // 判断文件夹是否存在
                if (!file.mkdirs()) { // 判断文件夹是否创建成功
                    // 文件夹创建失败，直接使用sd根目录
                    file = Environment.getExternalStorageDirectory();
                }
            }
            if (file.canWrite()) {
                String currentDBPath = "//data//" + context.getPackageName() + "//databases//" + databaseName + "";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(file, copyDbName);
                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    singleEmitter.onSuccess("");
                } else {
                    try {
                        singleEmitter.onError(new Error("the database " + databaseName + " file not exist !"));
                    } catch (Exception e) {
                        singleEmitter.onError(e);
                    }
                }
            }
        } catch (Exception e) {
            singleEmitter.onError(e);
        }
    }

}
