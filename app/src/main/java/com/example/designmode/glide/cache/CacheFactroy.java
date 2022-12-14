package com.example.designmode.glide.cache;

public interface CacheFactroy<T> {
    CacheHandler<T> createCacheHandler();
}
