package com.example.designmode.glide.cache;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.collection.LruCache;

import com.example.designmode.glide.resource.Value;

//内存缓存
public class MemoryCache extends LruCache<String, Value> {
    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public MemoryCache(int maxSize) {super(maxSize);}

    @Override
    protected int sizeOf(@NonNull String key, @NonNull Value value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            return value.getBitmap().getAllocationByteCount();
        return value.getBitmap().getByteCount();
    }
}
