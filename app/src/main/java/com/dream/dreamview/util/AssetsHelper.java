package com.dream.dreamview.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

@SuppressWarnings("unused")
public class AssetsHelper {
    public static final int BACKUP_SUCCESS = 1;
    public static final int FILE_BACKUP_EXISTS = 2; // 备份文件已存在

    private AssetsHelper() {
    }

    // 拷贝多个assets目录下文件
    public static Single<Integer> copyAssetsDB(final Context context, final String path) {
        return Single.create(new SingleOnSubscribe<Integer>() {
            @Override
            public void subscribe(SingleEmitter<Integer> singleEmitter) throws Exception {
                copyDB(context, path, singleEmitter);
            }
        });
    }

    // 拷贝单个assets目录下文件
    public static Single<Integer> copyAssetsDB(final Context context, @NonNull final String file, @NonNull final String dbName) {
        return Single.create(new SingleOnSubscribe<Integer>() {
            @Override
            public void subscribe(SingleEmitter<Integer> singleEmitter) throws Exception {
                copyDB(context, file, dbName, singleEmitter);
            }
        });
    }

    @SuppressWarnings("unused")
    private static void copyDB(Context context, String path, SingleEmitter<Integer> singleEmitter) {
        AssetManager assetManager = context.getAssets();
        String[] files;
        boolean isDir;
        try {
            files = assetManager.list(path);
            isDir = true;
        } catch (IOException e) {
            files = new String[]{path};
            isDir = false;
            singleEmitter.onError(e);
        }
        if (null == files) {
            return;
        }
        for (String filename : files) {
            String origin = filename;
            if (isDir && !TextUtils.isEmpty(path)) {
                origin = path + File.separator + origin;
            }
            String name = new File(origin).getName();
            copyDB(context, origin, name, singleEmitter);
        }
    }

    @SuppressWarnings({"unused", "ResultOfMethodCallIgnored"})
    private static void copyDB(Context context, @NonNull String file, @NonNull String dbName, SingleEmitter<Integer> singleEmitter) {
        File outFile = context.getDatabasePath(dbName);
        File parentFile = outFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        if (!outFile.exists() || outFile.length() <= 0) {
            InputStream in = null;
            FileOutputStream out = null;
            try {
                in = context.getAssets().open(file);
                outFile.createNewFile();
                out = new FileOutputStream(outFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
                singleEmitter.onSuccess(BACKUP_SUCCESS);
            } catch (IOException e) {
                singleEmitter.onError(e);
            } finally {
                try {
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            singleEmitter.onSuccess(FILE_BACKUP_EXISTS);
        }
    }


}
