package com.example.designmode.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtils {

    private static final String TAG = BitmapUtils.class.getSimpleName();
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

    public static Bitmap getBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            Log.d(TAG, "getBitmap: " + "null");
            return null;
        }
        Bitmap dst = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        if (bitmap.getConfig() == Bitmap.Config.ARGB_8888) {
            int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
            bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
            for (int i = 0; i < pixels.length; i++) {
                pixels[i] = pixels[i] & 0xffffff;
             }
            dst.setPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
            return dst;
        }
        return bitmap;
    }

    //Bitmap获取像素操作
    public static Bitmap grayPixels(Bitmap src) {
        Bitmap dst = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        Log.d(TAG, "grayPixels: " + src.getConfig());
        int[] pixels = new int[src.getWidth() * src.getHeight()];
        src.getPixels(pixels, 0, src.getWidth(), 0, 0, src.getWidth(), src.getHeight());
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
        src.getPixels(pixels, 0, src.getWidth(), 0, 0, src.getWidth(), src.getHeight());
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


    public static Bitmap readBitmapFromFile(String filePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        int inSampleSize = 1;
        if (srcHeight > height || srcWidth > width) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / height);
            } else {
                inSampleSize = Math.round(srcWidth / width);
            }
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeFile(filePath, options);
    }


    public static Bitmap readBitmapFromFileDescriptor(String filePath, int width, int height) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
            float srcWidth = options.outWidth;
            float srcHeight = options.outHeight; int inSampleSize = 1;
            if (srcHeight > height || srcWidth > width) {
                if (srcWidth > srcHeight) {
                    inSampleSize = Math.round(srcHeight / height);
                } else {
                    inSampleSize = Math.round(srcWidth / width);
                }
            }
            options.inJustDecodeBounds = false;
            options.inSampleSize = inSampleSize;
            return BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
        } catch (Exception ex) {
        }
        return null;
    }


    public static Bitmap readBitmapFromInputStream(InputStream ins, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;BitmapFactory.decodeStream(ins, null, options);
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        int inSampleSize = 1;
        if (srcHeight > height || srcWidth > width) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / height);
            } else {
                inSampleSize = Math.round(srcWidth / width);
            }
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeStream(ins, null, options);
    }


    public static Bitmap readBitmapFromResource(Resources resources, int resourcesId, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resourcesId, options);
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        int inSampleSize = 1;
        if (srcHeight > height || srcWidth > width) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / height);
            } else {
                inSampleSize = Math.round(srcWidth / width);
            }
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeResource(resources, resourcesId, options);
    }

    public static Bitmap readBitmapFromAssetsFile(Context context, String filePath) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(filePath);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static Bitmap readBitmapFromByteArray(byte[] data, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        int inSampleSize = 1;
        if (srcHeight > height || srcWidth > width) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / height);
            } else {
                inSampleSize = Math.round(srcWidth / width);
            }
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    //1. 计算缩放比例
    //w表示图片实际宽度，h表示图片实际高度
    //maxW表示图片在屏幕上可显示的最大宽度，maxH表示图片在屏幕上可显示的最大高度
    private static int calcuteInSampleSize(int w, int h, int maxW, int maxH) {
        int inSampleSize = 1;
        if (w > maxW && h > maxH) {
            inSampleSize = 2;
            while (w / inSampleSize > maxW && h / inSampleSize > maxH) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
    //2. 根据缩放比例，获取缩放后图片
    public static Bitmap resizeBitmap(Context context, int id, int maxW, int maxH, boolean hasAlpha) {
        Resources resources = context.getResources();
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 设置为true后，再去解析，就只解析 out 参数
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, id, options);
        int w = options.outWidth;
        int h = options.outHeight;
        options.inSampleSize = calcuteInSampleSize(w, h, maxW, maxH);
        if (!hasAlpha) {
            options.inPreferredConfig = Bitmap.Config.RGB_565;
        }
        options.inJustDecodeBounds = false;
        //根据缩放比例，获取缩放后图片
        return BitmapFactory.decodeResource(resources, id, options);
    }

    /**
     * 回收ImageView
     */
    public static void recycleImageView(View view) {
        if (view == null) return;
        if (view instanceof ImageView) {
            Drawable drawable = ((ImageView) view).getDrawable();
            if (drawable instanceof BitmapDrawable) {
                Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
                if (bmp != null && !bmp.isRecycled()) {
                    ((ImageView) view).setImageBitmap(null);
                    bmp.recycle();
                    bmp = null;
                }
            }
        }
    }



}
