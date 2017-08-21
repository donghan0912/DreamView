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

    private AssetsHelper() {
    }

    // 拷贝多个assets目录下文件
    public static Single<Object> copyAssetsDB(final Context context, final String path) {
        return Single.create(new SingleOnSubscribe<Object>() {
            @Override
            public void subscribe(SingleEmitter<Object> singleEmitter) throws Exception {
                copyDB(context, path, singleEmitter);
            }
        });
    }

    // 拷贝单个assets目录下文件
    public static Single<Object> copyAssetsDB(final Context context, @NonNull final String file, @NonNull final String dbName) {
        return Single.create(new SingleOnSubscribe<Object>() {
            @Override
            public void subscribe(SingleEmitter<Object> singleEmitter) throws Exception {
                copyDB(context, file, dbName, singleEmitter);
            }
        });
    }

    @SuppressWarnings("unused")
    private static void copyDB(Context context, String path, SingleEmitter<Object> singleEmitter) {
        AssetManager assetManager = context.getAssets();
        String[] files;
        boolean isDir;
        try {
            files = assetManager.list(path);
            isDir = true;
        } catch (IOException e) {
            LogUtil.e("Failed to get asset file list.", e);
            files = new String[]{path};
            isDir = false;
            singleEmitter.onError(e);
        }
        if (null == files) {
            try {
                singleEmitter.onError(new Error("file not exist"));
            } catch (Exception e) {
                singleEmitter.onError(e);
            }
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
    private static void copyDB(Context context, @NonNull String file, @NonNull String dbName, SingleEmitter<Object> singleEmitter) {
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
                singleEmitter.onSuccess("copy success");
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
        }
    }


}
