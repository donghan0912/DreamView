package com.dream.dreamview.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.UUID;

public class SystemUtils {

  public static String getVersionName(Context context) {
    PackageManager pm = context.getPackageManager();
    PackageInfo pi;
    String versionName = "";
    try {
      pi = pm.getPackageInfo(context.getPackageName(), 0);
      versionName = pi.versionName;
    } catch (NameNotFoundException e) {
      e.printStackTrace();
    }
    return versionName;
  }

  public static int getVersionCode(Context context) {
    PackageManager pm = context.getPackageManager();
    PackageInfo pi;
    int versionCode = 0;
    try {
      pi = pm.getPackageInfo(context.getPackageName(), 0);
      versionCode = pi.versionCode;
    } catch (NameNotFoundException e) {
      e.printStackTrace();
    }
    return versionCode;
  }

  @Deprecated
  public static String getDeviceUUID(Context context) {
    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

    @SuppressLint("HardwareIds")
    String tmDevice = tm.getDeviceId();
    String mac = getMacAddress(context);
    if (TextUtils.isEmpty(mac) || TextUtils.isEmpty(tmDevice)) {
      return "";
    }

    UUID deviceUuid = new UUID(mac.hashCode(), tmDevice.hashCode());
    return deviceUuid.toString();
  }

  @SuppressLint("HardwareIds")
  public static String getUUID(Context context) {
    final TelephonyManager tm = (TelephonyManager) context.getSystemService(
        Context.TELEPHONY_SERVICE);

    final String tmDevice, tmSerial, androidId;
    tmDevice = "" + tm.getDeviceId();
    tmSerial = "" + tm.getSimSerialNumber();
    androidId = "" + Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);

    UUID deviceUuid = new UUID(androidId.hashCode(),
        ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
    return deviceUuid.toString();
  }

  public static String getModel() {
    return Build.MODEL;
  }

  public static int getApiLevel() {
    return Build.VERSION.SDK_INT;
  }

  public static String getChannel(Context context) {
    try {
      Bundle bundle = context.getPackageManager()
          .getApplicationInfo(context.getPackageName(),
              PackageManager.GET_META_DATA).metaData;
      return String.valueOf(bundle.get("UMENG_CHANNEL"));
    } catch (NullPointerException e) {
      LogUtil.e("Failed to load meta-data, NullPointer: " + e.getMessage());
    } catch (NameNotFoundException e) {
      e.printStackTrace();
    }
    return "debug";
  }

  @SuppressWarnings("unused")
  @SuppressLint("HardwareIds")
  public static String getMacAddress(Context context) {
    WifiManager wifiManager = (WifiManager) context.getApplicationContext()
        .getSystemService(Context.WIFI_SERVICE);
    return wifiManager.getConnectionInfo().getMacAddress();
  }

  /** IMEI */
  @SuppressLint("HardwareIds")
  public static String getDeviceId(Context context) {
    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    return tm.getDeviceId();
  }

  /** IMSI */
  @SuppressLint("HardwareIds")
  public static String getSubscriberId(Context context) {
    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    return tm.getSubscriberId();
  }

  @SuppressLint("HardwareIds")
  public static String getSimSerialNumber(Context context) {
    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    return tm.getSimSerialNumber();
  }

  @SuppressLint("HardwareIds")
  public static String getSerialNumber() {
    return Build.SERIAL;
  }

  @SuppressLint("HardwareIds")
  public static String getAndroidId(Context context) {
    return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
  }

  public static String getPhoneModel() {
    return Build.BRAND + " " + Build.MODEL;
  }

  public static boolean isSamsungPhone() {
    if (getPhoneModel().contains("samsung")) {
      return true;
    }
    return false;
  }

  public static boolean isHuaweiPhone() {
    if (getPhoneModel().contains("huawei") || getPhoneModel().contains("honour")) {
      return true;
    }
    return false;
  }
}
