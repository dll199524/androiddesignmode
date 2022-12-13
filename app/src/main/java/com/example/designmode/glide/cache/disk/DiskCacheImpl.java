package com.example.designmode.glide.cache.disk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.example.designmode.glide.resource.Value;
import com.example.designmode.glide.utils.Util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//磁盘缓存
public class DiskCacheImpl {
    private static final String TAG = DiskCacheImpl.class.getSimpleName();
    private DiskLruCache diskLruCache;
    int appVersion = 1;
    int count = 1;
    int maxSize = 1024 * 1024 * 10;
    private static final String DISK_CACHE_DIR = "disk_cache_dir";
    public DiskCacheImpl() {
        //sd路径
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + DISK_CACHE_DIR);
        try {
            diskLruCache = DiskLruCache.open(file, appVersion, count, maxSize);
        } catch (Exception e) {e.printStackTrace();}
    }

    public void put(String key, Value val) {
        Util.checkNotEmpty(key);
        DiskLruCache.Editor editor = null;
        OutputStream os = null;
        try {
            editor = diskLruCache.edit(key);
            os = editor.newOutputStream(0);
            Bitmap bitmap = val.getBitmap();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                editor.abort();
            }  catch (IOException ex) {
                ex.printStackTrace();
                Log.e(TAG, "put: editor.abort() e:" + ex.getMessage());
            }
        } finally {
            try {
                editor.commit();
                diskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "put: editor.commit(); e:" + e.getMessage());
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "put: os.close(); e:" + e.getMessage());
                }
            }

        }
    }

    public Value get(String key) {
        Util.checkNotEmpty(key);
        InputStream is = null;
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
            if (snapshot != null) {
                Value value = new Value();
                is = snapshot.getInputStream(0);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                value.setBitmap(bitmap);
                value.setKey(key);
                return value;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "get: inputStream.close(); e:" + e.getMessage());
                }
            }
        }
        return null;
    }
}
