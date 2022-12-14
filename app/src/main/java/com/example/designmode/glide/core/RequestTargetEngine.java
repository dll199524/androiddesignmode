package com.example.designmode.glide.core;


import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.example.designmode.glide.binding.LifeCycleCallBack;
import com.example.designmode.glide.cache.ActiveCache;
import com.example.designmode.glide.cache.MemoryCache;
import com.example.designmode.glide.cache.disk.DiskCacheImpl;
import com.example.designmode.glide.cache.disk.DiskLruCache;
import com.example.designmode.glide.extra.LoadDataManager;
import com.example.designmode.glide.extra.ResponseListener;
import com.example.designmode.glide.resource.Key;
import com.example.designmode.glide.resource.Value;
import com.example.designmode.glide.resource.ValueCallback;
import com.example.designmode.glide.utils.Util;


public class RequestTargetEngine implements LifeCycleCallBack, ValueCallback, ResponseListener {

    private String path;
    private Context context;
    private String key;
    private ImageView imageView;
    private static final String TAG = RequestTargetEngine.class.getSimpleName();

    private ActiveCache activeCache;
    private MemoryCache memoryCache;
    private DiskCacheImpl diskLruCache;

    private static int MAX_SIZE = 1024 * 1024 * 60;

    public RequestTargetEngine() {
        if (activeCache == null) activeCache = new ActiveCache(this);
        if (memoryCache == null) memoryCache = new MemoryCache(MAX_SIZE);
        if (diskLruCache == null) diskLruCache = new DiskCacheImpl();
    }


    public void startLoadValue(String path, Context context) {
        this.path = path;
        this.context = context;
        this.key = new Key(path).getKey();
    }

    @Override
    public void initAction() {
        Log.d(TAG, "glide initAction start...... ");
    }

    @Override
    public void stopAction() {
        Log.d(TAG, "glide stopAction start...... ");
    }

    @Override
    public void recycleAction() {
        Log.d(TAG, "glide recycleAction start...... ");
    }

    @Override
    public void onSuccess(Value value) {
        if (value != null)
            saveCache(key, value);
        imageView.setImageBitmap(value.getBitmap());
    }

    private void saveCache(String key, Value value) {
        Log.d(TAG, "saveCache: >>>>>>>>>>>>>>>>>>>>>>>>>> 加载外置资源成功后 ，保存到缓存中， key:" + key + " value:" + value);
        value.setKey(key);
        if (diskLruCache != null) diskLruCache.put(key, value);
    }

    public void into(ImageView imageView) {
        this.imageView = imageView;
        Util.checkNotEmpty(imageView);
        Util.assertMainThread();
        Value value = cacheAction();
        if (value != null) imageView.setImageBitmap(value.getBitmap());

    }

    private Value cacheAction() {
        Value value = activeCache.get(key);
        if (value != null) {
            Log.d(TAG, "cacheAction: 本次加载的是在（活动缓存）中获取的资源>>>");
            return value;
        }

        value = memoryCache.get(key);
        if (value != null) {
            Log.d(TAG, "cacheAction: 本次加载的是在（内存缓存）中获取的资源>>>");
            activeCache.put(key, value);
            memoryCache.remove(key);
            return value;
        }

        value = diskLruCache.get(key);
        if (value != null) {
            Log.d(TAG, "cacheAction: 本次加载的是在（活动缓存）中获取的资源>>>");
            activeCache.put(key, value);
            return value;
        }
        value = new LoadDataManager().loadResource(path, this, context);
        if (value != null) return value;
        return null;
    }


    @Override
    public void onFailure(Exception e) {
        Log.d(TAG, "request onFailure: " + e.getMessage());
    }

    @Override
    public void onValueNotUseListener(String key, Value value) {
        if (key != null && value != null) memoryCache.put(key, value);
    }
}
