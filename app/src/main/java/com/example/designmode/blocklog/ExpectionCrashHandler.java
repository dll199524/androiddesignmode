package com.example.designmode.blocklog;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExpectionCrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "ExceptionCrashHandler";
    private volatile static ExpectionCrashHandler mInstance;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private WeakReference<Context> mContext;

    public void init(Context context) {
        Thread.currentThread().setUncaughtExceptionHandler(this);
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.mContext = new WeakReference<>(context);
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        Log.d(TAG, "拦截到闪退信息: " + e.getMessage());
        String crashFileName = saveCacheInfo(e);
        Log.d(TAG, "fileName" + crashFileName);
        mDefaultHandler.uncaughtException(t, e);
    }

    private String saveCacheInfo(Throwable e) {
        String fileName = null;
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : obtainSimpleInfo(mContext.get()).entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            sb.append(key).append("=").append(val).append("\n");
        }
        sb.append(obtainExceptionInfo(e));
        File dir = new File(mContext.get().getFilesDir() + File.separator +
                "crash" + File.separator);
        if (dir.exists())
            deleteDir(dir);
        if (!dir.exists()) {
            boolean res = dir.mkdir();
            if (!res)
                Log.d(TAG, "saveCacheInfo: create file failed");
        }
        try {
            fileName = dir.toString() + File.separator
                    + getAssignTime() + ".txt";
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(sb.toString().getBytes());
            fos.flush();
            fos.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return fileName;
    }

    private String getAssignTime() {
        DateFormat dataFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm", Locale.US);
        long currentTime = System.currentTimeMillis();
        return dataFormat.format(currentTime);
    }

    private void deleteDir(File file) {
        boolean res;
        if (file.isFile()) {
            res = file.delete();
            if (!res)
                Log.d(TAG, "deleteDir: failed when delete" + file);
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                res = file.delete();
                if (!res)
                    Log.d(TAG, "deleteDir: failed when delete" + file);
                return;
            }
            for (File childFile : childFiles)
                deleteDir(childFile);
            res = file.delete();
            if (!res)
                Log.d(TAG, "deleteDir: failed when delete" + file);
        }
    }

    private String obtainExceptionInfo(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.close();
        return sw.toString();
    }

    private HashMap<String, String> obtainSimpleInfo(Context context) {
        HashMap<String, String> map = new HashMap<>();
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        map.put("versionName", packageInfo.versionName);
        map.put("versionCode", "" + packageInfo.versionCode);
        map.put("MODEL", Build.MODEL);
        map.put("SDK_INT", "" + Build.VERSION.SDK_INT);
        map.put("PRODUCT", Build.PRODUCT);
        map.put("MODEL_INFO", getMoblieInfo());
        return map;
    }

    private String getMoblieInfo() {
        StringBuilder sb = new StringBuilder();
        try {
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                String val = field.get(null).toString();
                sb.append(name).append("=").append(val).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    public static ExpectionCrashHandler getInstance() {
        if (mInstance == null) {
            synchronized (ExpectionCrashHandler.class) {
                if (mInstance == null)
                    mInstance = new ExpectionCrashHandler();
            }
        }
        return mInstance;
    }

    public ExpectionCrashHandler() {}


}
