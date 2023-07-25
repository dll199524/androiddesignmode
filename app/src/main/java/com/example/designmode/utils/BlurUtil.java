package com.example.designmode.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.WindowManager;

public class BlurUtil {
    private static final float BITMAP_SCALE = 0.4f;
    private static final int BLUR_RADIUS = 7;
    /**
     * 高斯模糊半径，0-25区间，越大越模糊
     */
    public static final int BLUR_RADIUS_MAX = 25;

    public static Bitmap blur(Context context, Bitmap bitmap) {
        return blur(context, bitmap, BITMAP_SCALE, BLUR_RADIUS);
    }

    public static Bitmap blur(Context context, Bitmap bitmap, float bitmap_scale) {
        return blur(context, bitmap, bitmap_scale, BLUR_RADIUS);
    }

    public static Bitmap blur(Context context, Bitmap bitmap, int blur_radius) {
        return blur(context, bitmap, BITMAP_SCALE, blur_radius);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blur(Context context, Bitmap bitmap, float bitmap_scale, int blur_radius) {
        Bitmap inputBitmap = Bitmap.createScaledBitmap(bitmap, Math.round(bitmap.getWidth() * bitmap_scale),
                Math.round(bitmap.getHeight() * bitmap_scale), false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(blur_radius);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        rs.destroy();
        bitmap.recycle();

        return outputBitmap;
    }

    /**
     * SurfaceControl makeDimLayer()
     */
    public static void activitySetBlur(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            activity.getWindow().setBackgroundBlurRadius(100);
        }
    }
}
