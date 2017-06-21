package com.dream.dreamview.util;

import android.util.Log;

import java.util.Locale;

/**
 * Log工具，类似android.util.Log。 tag自动产生，格式: customTagPrefix:className.methodName(L:lineNumber),
 * customTagPrefix为空时只输出：className.methodName(L:lineNumber)。
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class LogUtil {
    @SuppressWarnings("unused")
    private static final boolean DEBUG = true;

    @SuppressWarnings("unused")
    public static final boolean allowD = DEBUG;
    @SuppressWarnings("unused")
    public static final boolean allowE = true;
    @SuppressWarnings("unused")
    public static final boolean allowI = DEBUG;
    @SuppressWarnings("unused")
    public static final boolean allowV = DEBUG;
    @SuppressWarnings("unused")
    public static final boolean allowW = DEBUG;
    @SuppressWarnings("unused")
    public static final boolean allowWtf = true;

    @SuppressWarnings("unused")
    private LogUtil() {
    }

    @SuppressWarnings("unused")
    private static String generateTag() {
        StackTraceElement caller = new Throwable().getStackTrace()[2];
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(Locale.getDefault(), tag, callerClazzName,
                caller.getMethodName(), caller.getLineNumber());
        return tag;
    }

    @SuppressWarnings("unused")
    public static void d(String content) {
        if (!allowD) {
            return;
        }
        String tag = generateTag();

        Log.d(tag, content);
    }

    @SuppressWarnings("unused")
    public static void d(String content, Throwable tr) {
        if (!allowD) {
            return;
        }
        String tag = generateTag();

        Log.d(tag, content, tr);
    }

    @SuppressWarnings("unused")
    public static void d(String msg, Object... args) {
        if (!allowD) {
            return;
        }
        String tag = generateTag();

        Log.d(tag, String.format(msg, args));
    }

    @SuppressWarnings("unused")
    public static void e(String content) {
        if (!allowE) {
            return;
        }
        String tag = generateTag();

        Log.e(tag, content);
    }

    @SuppressWarnings("unused")
    public static void e(String content, Throwable tr) {
        if (!allowE) {
            return;
        }
        String tag = generateTag();

        Log.e(tag, content, tr);
    }

    @SuppressWarnings("unused")
    public static void e(String msg, Object... args) {
        if (!allowD) {
            return;
        }
        String tag = generateTag();

        Log.e(tag, String.format(msg, args));
    }

    @SuppressWarnings("unused")
    public static void i(String content) {
        if (!allowI) {
            return;
        }
        String tag = generateTag();

        Log.i(tag, content);
    }

    @SuppressWarnings("unused")
    public static void i(String content, Throwable tr) {
        if (!allowI) {
            return;
        }
        String tag = generateTag();

        Log.i(tag, content, tr);
    }

    @SuppressWarnings("unused")
    public static void i(String msg, Object... args) {
        if (!allowD) {
            return;
        }
        String tag = generateTag();

        Log.i(tag, String.format(msg, args));
    }

    @SuppressWarnings("unused")
    public static void v(String content) {
        if (!allowV) {
            return;
        }
        String tag = generateTag();

        Log.v(tag, content);
    }

    @SuppressWarnings("unused")
    public static void v(String content, Throwable tr) {
        if (!allowV) {
            return;
        }
        String tag = generateTag();

        Log.v(tag, content, tr);
    }

    @SuppressWarnings("unused")
    public static void v(String msg, Object... args) {
        if (!allowD) {
            return;
        }
        String tag = generateTag();

        Log.v(tag, String.format(msg, args));
    }

    @SuppressWarnings("unused")
    public static void w(String content) {
        if (!allowW) {
            return;
        }
        String tag = generateTag();

        Log.w(tag, content);
    }

    @SuppressWarnings("unused")
    public static void w(String content, Throwable tr) {
        if (!allowW) {
            return;
        }
        String tag = generateTag();

        Log.w(tag, content, tr);
    }

    @SuppressWarnings("unused")
    public static void w(Throwable tr) {
        if (!allowW) {
            return;
        }
        String tag = generateTag();

        Log.w(tag, tr);
    }

    @SuppressWarnings("unused")
    public static void w(String msg, Object... args) {
        if (!allowD) {
            return;
        }
        String tag = generateTag();

        Log.w(tag, String.format(msg, args));
    }

    @SuppressWarnings("unused")
    public static void wtf(String content) {
        if (!allowWtf) {
            return;
        }
        String tag = generateTag();

        Log.wtf(tag, content);
    }

    @SuppressWarnings("unused")
    public static void wtf(String content, Throwable tr) {
        if (!allowWtf) {
            return;
        }
        String tag = generateTag();

        Log.wtf(tag, content, tr);
    }

    @SuppressWarnings("unused")
    public static void wtf(Throwable tr) {
        if (!allowWtf) {
            return;
        }
        String tag = generateTag();

        Log.wtf(tag, tr);
    }

    @SuppressWarnings("unused")
    public static void wtf(String msg, Object... args) {
        if (!allowD) {
            return;
        }
        String tag = generateTag();

        Log.wtf(tag, String.format(msg, args));
    }
}
