package com.dream.dreamview.secret;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCrypt {
    private static final int CACHE_SIZE = 2048;
    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String PASS_KEY = "472ba9f6b0a20cf8";

    private Cipher mCipher;
    private SecretKeySpec mKey;
    private AlgorithmParameterSpec mSpec;

    public AESCrypt() {
        try {
            mCipher = Cipher.getInstance(CIPHER_ALGORITHM);
            mKey = new SecretKeySpec(PASS_KEY.getBytes("UTF-8"), KEY_ALGORITHM);
            mSpec = getIV();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public AESCrypt(String passKey) throws Exception {
        mCipher = Cipher.getInstance(CIPHER_ALGORITHM);
        mKey = new SecretKeySpec(passKey.getBytes("UTF-8"), KEY_ALGORITHM);
        mSpec = getIV();
    }

    private AlgorithmParameterSpec getIV() {
        byte[] iv = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00};
        return new IvParameterSpec(iv);
    }

    public synchronized String encrypt(String plainText) throws Exception {
        byte[] encrypted = encrypt(plainText.getBytes("UTF-8"));
        return new String(Base64.encode(encrypted, Base64.NO_PADDING | Base64.NO_WRAP |
                Base64.URL_SAFE), "UTF-8");
    }

    public synchronized String decrypt(String cryptText) throws Exception {
        byte[] bytes = Base64.decode(cryptText.getBytes("UTF-8"), Base64.NO_PADDING | Base64.NO_WRAP |
                Base64.URL_SAFE);
        byte[] decrypted = decrypt(bytes);
        return new String(decrypted, "UTF-8");
    }

    @SuppressWarnings({"WeakerAccess"})
    public synchronized byte[] encrypt(byte[] origin) throws Exception {
        mCipher.init(Cipher.ENCRYPT_MODE, mKey, mSpec);
        return mCipher.doFinal(origin);
    }

    @SuppressWarnings({"WeakerAccess"})
    public synchronized byte[] decrypt(byte[] origin) throws Exception {
        mCipher.init(Cipher.DECRYPT_MODE, mKey, mSpec);
        return mCipher.doFinal(origin);
    }

    /**
     * 文件加密
     *
     * @param srcFile    源文件
     * @param targetFile 加密后文件
     * @throws Exception
     */
    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    public synchronized void encrypt(File srcFile, File targetFile) throws Exception {
        mCipher.init(Cipher.ENCRYPT_MODE, mKey, mSpec);

        if (srcFile.exists()) {
            if (targetFile.exists()) {
                targetFile.delete();
            } else if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }

            FileInputStream fis = new FileInputStream(srcFile);
            FileOutputStream fos = new FileOutputStream(targetFile);

            CipherInputStream cin = new CipherInputStream(fis, mCipher);
            try {
                byte[] cache = new byte[CACHE_SIZE];
                int nRead;
                while ((nRead = cin.read(cache)) != -1) {
                    fos.write(cache, 0, nRead);
                    fos.flush();
                }
            } finally {
                safeClose(fos);
                safeClose(fis);
                safeClose(cin);
            }
        }
    }

    /**
     * 文件解密
     *
     * @param srcFile    源文件
     * @param targetFile 加密后文件
     * @throws Exception
     */
    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    public synchronized void decrypt(File srcFile, File targetFile) throws Exception {
        mCipher.init(Cipher.DECRYPT_MODE, mKey, mSpec);

        if (srcFile.exists()) {
            if (targetFile.exists()) {
                targetFile.delete();
            } else if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }

            FileInputStream fis = new FileInputStream(srcFile);
            FileOutputStream fos = new FileOutputStream(targetFile);

            CipherInputStream cin = new CipherInputStream(fis, mCipher);
            try {
                byte[] cache = new byte[CACHE_SIZE];
                int nRead;
                while ((nRead = cin.read(cache)) != -1) {
                    fos.write(cache, 0, nRead);
                    fos.flush();
                }
            } finally {
                safeClose(fos);
                safeClose(fis);
                safeClose(cin);
            }
        }
    }

    /**
     * 文件解密
     *
     * @param srcFile    源文件
     * @param targetFile 加密后文件
     * @throws Exception
     */
    @SuppressWarnings({"unused", "ResultOfMethodCallIgnored"})
    public synchronized void decryptPart(File srcFile, File targetFile) throws Exception {
        mCipher.init(Cipher.DECRYPT_MODE, mKey, mSpec);

        if (srcFile.exists()) {
            if (targetFile.exists()) {
                targetFile.delete();
            } else if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }

            FileInputStream fis = new FileInputStream(srcFile);
            FileOutputStream fos = new FileOutputStream(targetFile);

            try {
                int size = (CACHE_SIZE / 16 + 1) * 16;
                byte[] cache = new byte[size];

                int nRead = fis.read(cache);
                System.out.println("origin: " + cache.length + " : " + new String(cache));
                if (nRead < size && nRead > 0) {
                    byte[] tmp = new byte[nRead];
                    System.arraycopy(cache, 0, tmp, 0, tmp.length);
                    fos.write(decrypt(tmp));
                    fos.flush();
                } else {
                    byte[] decrypt = decrypt(cache);
                    System.out.println("decrypt: " + decrypt.length + " : " + new String(decrypt));
                    fos.write(decrypt);
                    fos.flush();
                }
                while ((nRead = fis.read(cache)) != -1) {
                    fos.write(cache, 0, nRead);
                    fos.flush();
                }
            } finally {
                safeClose(fis);
                safeClose(fos);
            }
        }
    }

    /**
     * 文件加密
     *
     * @param srcFile    源文件
     * @param targetFile 加密后文件
     * @throws Exception
     */
    @SuppressWarnings({"unused", "ResultOfMethodCallIgnored"})
    public synchronized void encryptPart(File srcFile, File targetFile) throws Exception {
        mCipher.init(Cipher.ENCRYPT_MODE, mKey, mSpec);

        if (srcFile.exists()) {
            if (targetFile.exists()) {
                targetFile.delete();
            } else if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }

            FileInputStream fis = new FileInputStream(srcFile);
            FileOutputStream fos = new FileOutputStream(targetFile);

            try {
                byte[] cache = new byte[CACHE_SIZE];
                int nRead = fis.read(cache);
                System.out.println("origin: " + cache.length + " : " + new String(cache));
                if (nRead < CACHE_SIZE && nRead > 0) {
                    byte[] tmp = new byte[nRead];
                    System.arraycopy(cache, 0, tmp, 0, tmp.length);
                    fos.write(encrypt(tmp));
                    fos.flush();
                } else {
                    byte[] encrypt = encrypt(cache);
                    System.out.println("encrypt: " + encrypt.length + " : " + new String(encrypt));
                    fos.write(encrypt);
                    fos.flush();
                }
                while ((nRead = fis.read(cache)) != -1) {
                    fos.write(cache, 0, nRead);
                    fos.flush();
                }
            } finally {
                safeClose(fis);
                safeClose(fos);
            }
        }
    }

    private void safeClose(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                // Silent
            }
        }
    }

    private void safeClose(OutputStream out) {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                // Silent
            }
        }
    }
}
