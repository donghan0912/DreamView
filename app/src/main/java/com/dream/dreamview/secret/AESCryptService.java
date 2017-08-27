package com.dream.dreamview.secret;

import android.support.v4.util.LruCache;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class AESCryptService {
    private static final int DEFAULT_SERVICE_COUNT = 5;

    private static class SingletonHolder {
        static final AESCryptService INSTANCE = new AESCryptService();
    }

    public static AESCryptService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private final AESCrypt mEncrypt;
    private final AESCrypt mDecrypt;
    private final LruCache<String, AESCrypt> mEncrypts;
    private final LruCache<String, AESCrypt> mDecrypts;

    private AESCryptService() {
        mEncrypt = new AESCrypt();
        mDecrypt = new AESCrypt();
        mEncrypts = new LruCache<>(DEFAULT_SERVICE_COUNT);
        mDecrypts = new LruCache<>(DEFAULT_SERVICE_COUNT);
    }

    public String encrypt(String msg) {
        try {
            return mEncrypt.encrypt(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    public String decrypt(String msg) {
        try {
            return mDecrypt.decrypt(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    public String encrypt(String key, String msg) {
        String lruKey = generateMD5String(key);
        AESCrypt encrypt = mEncrypts.get(lruKey);
        try {
            if (null == encrypt) {
                encrypt = new AESCrypt(key);
                mEncrypts.put(lruKey, encrypt);
            }
            return encrypt.encrypt(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    public String decrypt(String key, String msg) {
        String lruKey = generateMD5String(key);
        try {
            AESCrypt decrypt = mDecrypts.get(lruKey);
            if (null == decrypt) {
                decrypt = new AESCrypt(key);
                mDecrypts.put(lruKey, decrypt);
            }
            return decrypt.decrypt(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    public void decrypt(String key, File src, File dest) {
        String lruKey = generateMD5String(key);
        try {
            AESCrypt decrypt = mDecrypts.get(lruKey);
            if (null == decrypt) {
                decrypt = new AESCrypt(key);
                mDecrypts.put(lruKey, decrypt);
            }
            decrypt.decrypt(src, dest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String generateMD5String(String stringToEncode) {
        MessageDigest digester;
        try {
            digester = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) {
            throw new RuntimeException(nsae);
        }
        try {
            digester.update(stringToEncode.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return toHexString(digester.digest(), 32);
    }

    public static String toHexString(byte[] bytes, int lengthToPad) {
        BigInteger hash = new BigInteger(1, bytes);
        String digest = hash.toString(16);

        while (digest.length() < lengthToPad) {
            digest = "0" + digest;
        }
        return digest;
    }
}
