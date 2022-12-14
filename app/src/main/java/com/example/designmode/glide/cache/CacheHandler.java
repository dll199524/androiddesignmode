package com.example.designmode.glide.cache;

public interface CacheHandler<T> {
    void get(T key);
    void put(T key, T val);
}
