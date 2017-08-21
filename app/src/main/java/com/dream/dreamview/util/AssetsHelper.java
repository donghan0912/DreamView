package com.dream.dreamview.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("unused")
public class AssetsHelper {
    private AssetsHelper() {
    }

    @SuppressWarnings("unused")
    public static void copyDB(Context context, String path) {
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
            copyDB(context, origin, name);
        }
    }

    @SuppressWarnings({"unused", "ResultOfMethodCallIgnored"})
    public static void copyDB(Context context, @NonNull String file, @NonNull String dbName) {
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
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    out.flush();
                    out.close();
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
