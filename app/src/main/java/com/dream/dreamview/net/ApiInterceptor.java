package com.dream.dreamview.net;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;

import com.dream.dreamview.BuildConfig;
import com.dream.dreamview.util.LogUtil;
import com.dream.dreamview.util.SystemUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * okhttp 请求头 拦截器
 */
public class ApiInterceptor implements Interceptor {
    @SuppressWarnings("unused")
    private static final boolean DEBUG = BuildConfig.DV_DEBUG;

    @SuppressWarnings("unused")
    private static String sToken;

    @SuppressWarnings("unused")
    private static final String PLATFORM_ANDROID = "1"; // Android 为 1；IOS为 2
    @SuppressWarnings("unused")
    private static final String MD5_KEY = "b39401f1";
    /**
     * t: ${token} Token
     * a: ${app} 应用，超级万学为: 1;考研高手为:2;ace:3...
     * v: ${version} 应用版本号，数字
     * n: ${versionName} 应用版本名, 字符串
     * p: ${platform} 平台，Android为 1;IOS为 2
     * m: ${model} 设备描述信息，如MI 2，iPhone 4
     * s: ${sdk} 系统SDK版本
     * u: ${uuid} 设备唯一标识(IOS为登录后中台返回)
     * d: ${time} 时间戳
     * c: ${md5-check} 校验信息(可选)
     * h: ${channel} 渠道
     */
    @SuppressWarnings("unused")
    private static final String HEADER_TOKEN = "t";
    @SuppressWarnings("unused")
    private static final String HEADER_APP = "a";
    @SuppressWarnings("unused")
    private static final String HEADER_APP_VERSION = "v";
    @SuppressWarnings("unused")
    private static final String HEADER_APP_VERSION_NAME = "n";
    @SuppressWarnings("unused")
    private static final String HEADER_DEVICE_TYPE = "p";
    @SuppressWarnings("unused")
    private static final String HEADER_DEVICE_NAME = "m";
    @SuppressWarnings("unused")
    private static final String HEADER_DEVICE_SDK = "s";
    @SuppressWarnings("unused")
    private static final String HEADER_UUID = "u";
    @SuppressWarnings("unused")
    private static final String HEADER_TIME = "d";
    @SuppressWarnings("unused")
    private static final String HEADER_MD5 = "c";
    @SuppressWarnings("unused")
    private static final String HEADER_CHANNEL = "h";

    @SuppressWarnings("unused")
    private static final String HEADER_ACCEPT = "Accept";
    @SuppressWarnings("unused")
    private static final String HEADER_ACCEPT_JSON = "application/json";

    @SuppressWarnings("unused")
    private final String mModel;
    @SuppressWarnings("unused")
    private final String mSdk;
    @SuppressWarnings("unused")
    private final String mAppVersion;
    @SuppressWarnings("unused")
    private final String mAppVersionName;
    @SuppressWarnings("unused")
    private final String mChannel;
//    @SuppressWarnings("unused")
//    private final String mApp;
    @SuppressWarnings("unused")
    private final String mUUID;
    @SuppressWarnings("unused")
    private boolean mHasToken;

    @SuppressWarnings("unused")
    private final Context mContext;

    public ApiInterceptor(Context context) {
        mContext = context.getApplicationContext();
        String model = "";
        try {
            model = URLEncoder.encode(SystemUtils.getModel(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            String tmp = SystemUtils.getModel();
            for (int i = 0, length = tmp.length(); i < length; i++) {
                char c = tmp.charAt(i);
                if (c > '\u001f' && c < '\u007f') {
                    model += c;
                }
            }
        }
        mModel = model;
        mSdk = String.valueOf(SystemUtils.getApiLevel());
        mAppVersion = String.valueOf(SystemUtils.getVersionCode(mContext));
        mAppVersionName = SystemUtils.getVersionName(mContext);
        mChannel = getChannel();
        mUUID = SystemUtils.getUUID(mContext);

        mHasToken = false;
    }

    public ApiInterceptor(Context context, boolean token) {
        this(context);
        mHasToken = token;
    }

    @SuppressWarnings("unused")
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
//        String time = String.valueOf(CommonPreferences.getServerTime());
        String time = String.valueOf(System.currentTimeMillis());


        Request.Builder requestBuilder = original.newBuilder()
             .header(HEADER_APP_VERSION, mAppVersion)
             .header(HEADER_APP_VERSION_NAME, mAppVersionName)
             .header(HEADER_DEVICE_TYPE, PLATFORM_ANDROID)
             .header(HEADER_DEVICE_NAME, mModel)
             .header(HEADER_DEVICE_SDK, mSdk)
             .header(HEADER_UUID, mUUID)
             .header(HEADER_TIME, time)
             .header(HEADER_CHANNEL, mChannel)
             .header(HEADER_ACCEPT, HEADER_ACCEPT_JSON)
             .method(original.method(), original.body());

        if (mHasToken && !TextUtils.isEmpty(sToken)) {
            requestBuilder.header(HEADER_TOKEN, sToken);
        }

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }

    private String getChannel() {
        try {
            Bundle bundle = mContext.getPackageManager()
                                    .getApplicationInfo(mContext.getPackageName(),
                                            PackageManager.GET_META_DATA).metaData;
            return String.valueOf(bundle.get("UMENG_CHANNEL"));
        } catch (NullPointerException e) {
            if (DEBUG) {
                LogUtil.e("Failed to load meta-data, NullPointer: " + e.getMessage());
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "debug";
    }

    public static void updateToken(String token) {
        sToken = token;
    }
}
