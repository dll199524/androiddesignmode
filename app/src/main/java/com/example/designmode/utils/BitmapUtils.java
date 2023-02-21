package com.example.designmode.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import java.io.File;

public class BitmapUtils {

    //ColorMatrix矩阵运算 色彩：针对每个单个处理
    public static Bitmap gray(Bitmap src) {
        Bitmap dst = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src
                .getConfig());
        Canvas canvas = new Canvas(dst);

        Paint paint = new Paint();
        paint.setDither(false);
        paint.setAntiAlias(true);

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(dst, 0, 0, paint);
        return dst;
    }

    //Bitmap获取像素操作
    public static Bitmap grayPixels(Bitmap src) {
        Bitmap dst = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        int[] pixels = new int[src.getWidth() * src.getHeight()];
        src.setPixels(pixels, 0, src.getWidth(), 0, 0, src.getWidth(), src.getHeight());
        for (int i = 0; i < pixels.length; i++) {
            int pixel = pixels[i];
            int a = (pixel >> 24) & 0xff;
            int r = (pixel >> 16) & 0xff;
            int g = (pixel >> 8) & 0xff;
            int b = pixel & 0xff;

            int gray = (int)(0.213f * r + 0.715f * g + 0.072f * b);
            pixels[i] = (a << 24) | (gray << 16) | (gray << 8) | gray;
        }
        dst.setPixels(pixels, 0, src.getWidth(), 0, 0, src.getWidth(), src.getHeight());
        return dst;
    }

    public static Bitmap garyByPixels(Bitmap src) {
        Bitmap dst = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        int[] pixels = new int[src.getWidth() * src.getHeight()];
        src.setPixels(pixels, 0, src.getWidth(), 0, 0, src.getWidth(), src.getHeight());
        for (int i = 0; i < pixels.length; i++) {
            int pixel = pixels[i];
            int a = (pixel >> 24) & 0xff;
            int r = (pixel >> 16) & 0xff;
            int g = (pixel >> 8) & 0xff;
            int b = pixel & 0xff;

            int gray = (r + g + b) / 3;
            pixels[i] = gray;
        }
        dst.setPixels(pixels, 0, src.getWidth(), 0, 0, src.getWidth(), src.getHeight());
        return dst;
    }

    //高斯模糊


    //黑白板



}
