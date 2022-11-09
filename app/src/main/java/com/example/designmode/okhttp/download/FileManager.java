package com.example.designmode.okhttp.download;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.security.MessageDigest;

public class FileManager {

    private static volatile FileManager instance;
    private Context context;
    private File rootDir;


    public static FileManager getInstance() {
        if (instance == null) {
            synchronized (FileManager.class) {
                if (instance == null) instance = new FileManager();
            }
        }
        return instance;
    }

    public void init(Context context) {this.context = context;}

    public void rootDir(File file) {
        if (!file.exists()) file.mkdirs();
        if (file.exists() && file.isDirectory()) rootDir = file;
    }

    public File getFile(String url) {
        String fileName = getMd5(url);
        if (rootDir == null) rootDir = context.getCacheDir();
        File file = new File(rootDir, fileName);
        return file;
    }

    private String getMd5(String url) {
        if (TextUtils.isEmpty(url)) return null;
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("md5");
            messageDigest.update(url.getBytes());
            byte[] cipher = messageDigest.digest();
            for (byte b : cipher) {
                String hexStr = Integer.toHexString(b & 0xff);
                sb.append(hexStr.length() == 1 ? "0" + hexStr : hexStr);
            }
        } catch (Exception e) {e.printStackTrace();}
        return sb.toString();
    }
}
