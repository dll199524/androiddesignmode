package com.example.plugin;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.lang.reflect.Method;

public class LoadUtil {

    private final static String apkPath = "/sdcard/plugin-debug.apk";
    private static Resources mResources;

    public static Resources getResources(Context context) {
        if (mResources == null) mResources = loadResources(context);
        return mResources;
    }

    private static Resources loadResources(Context context) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method method = AssetManager.class.getMethod("setApkAssets");
            method.invoke(assetManager, apkPath);

            Resources resources = context.getResources();
            return new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
        } catch (Exception e) {e.printStackTrace();}
        return null;
    }
}
