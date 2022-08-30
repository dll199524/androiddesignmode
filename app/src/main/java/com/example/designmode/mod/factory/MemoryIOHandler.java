package com.example.designmode.mod.factory;

import android.util.LruCache;

public class MemoryIOHandler implements IOHandler{

    private static LruCache<String, Object> mCache = new LruCache<>(10 * 1024 *1024);
    @Override
    public void save(String key, String val) {

    }

    @Override
    public void save(String key, double val) {

    }

    @Override
    public void save(String key, int val) {

    }

    @Override
    public void save(String key, long val) {

    }

    @Override
    public void save(String key, boolean val) {

    }

    @Override
    public void save(String key, Object val) {

    }

    @Override
    public String getString(String key) {
        return null;
    }

    @Override
    public double getDouble(String key, double defaultVal) {
        return 0;
    }

    @Override
    public int getInt(String key, int defaultVal) {
        return 0;
    }

    @Override
    public long getLong(String key, long defaultVal) {
        return 0;
    }

    @Override
    public boolean getBoolean(String key, boolean defaultVal) {
        return false;
    }

    @Override
    public Object getObject(String key, Object defaultVal) {
        return null;
    }
}
