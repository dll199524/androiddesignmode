package com.example.designmode.performance.ui;

import android.app.Activity;
import android.app.Application;
import android.util.DisplayMetrics;

import java.lang.reflect.Field;

public class AppDensity {

    public static void setAppDensity(Activity activity, Application application, float designDp) {
        final DisplayMetrics sourceDisplayMetrics = application.getResources().getDisplayMetrics();

        //宽度为designDp的设计稿
        final float targetDensity = sourceDisplayMetrics.widthPixels / designDp;
        final int targetDpi = (int) (targetDensity * 160);
        sourceDisplayMetrics.density = targetDensity;
        sourceDisplayMetrics.densityDpi = targetDpi;

        //activity的DisplayMetrics也要设置
//        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
//        activityDisplayMetrics.density = targetDensity;
//        activityDisplayMetrics.densityDpi = targetDpi;

        //设置bitmap density
        setBitmapDensity((int) targetDensity);
    }

    public static void setActivityDensity(Activity activity, Application application, float designDp) {
        final DisplayMetrics sourceDisplayMetrics = application.getResources().getDisplayMetrics();

        //宽度为designDp的设计稿
        final float targetDensity = sourceDisplayMetrics.widthPixels / designDp;
        final int targetDpi = (int) (targetDensity * 160);
        sourceDisplayMetrics.density = targetDensity;
        sourceDisplayMetrics.densityDpi = targetDpi;

        //activity的DisplayMetrics也要设置
        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.densityDpi = targetDpi;

        //设置bitmap density
        setBitmapDensity((int) targetDensity);
    }

    //修改bitmap默认屏幕密度
    private static void setBitmapDensity(int targetDensity) {
        Class clazz;
        try {
            clazz = Class.forName("android.graphics.Bitmap");
            Field field = clazz.getField("sDefaultDensity");
            field.setAccessible(true);
            field.set(null, targetDensity);
            field.setAccessible(false);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {e.printStackTrace();}
    }

}
