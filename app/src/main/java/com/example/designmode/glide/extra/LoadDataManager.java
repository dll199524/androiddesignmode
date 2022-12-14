package com.example.designmode.glide.extra;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.designmode.glide.resource.Value;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LoadDataManager implements ILoadData, Runnable{

    String path;
    ResponseListener listener;
    Context context;
    private static final String TAG = LoadDataManager.class.getSimpleName();

    @Override
    public Value loadResource(String path, ResponseListener listener, Context context) {
        this.path = path;
        this.listener = listener;
        this.context = context;
        Uri uri = Uri.parse(path);
        if (uri.getScheme().equalsIgnoreCase("HTTP") || uri.getScheme().equalsIgnoreCase("HTTPS"))
            new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                    new SynchronousQueue<>()).execute(this);

        return null;
    }

    @Override
    public void run() {
        InputStream is = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(path);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(5000);
            final int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                is = httpURLConnection.getInputStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(is);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Value value = new Value();
                        value.setBitmap(bitmap);
                        listener.onSuccess(value);
                    }
                });
            } else {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFailure(new IllegalArgumentException("请求失败" + responseCode));
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "run: 关闭 inputStream.close(); e:" + e.getMessage());
            }
            if (httpURLConnection != null) httpURLConnection.disconnect();
        }
    }


}
