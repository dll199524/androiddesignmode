package com.example.designmode.glide.cache;

import com.example.designmode.glide.resource.Value;
import com.example.designmode.glide.resource.ValueCallback;
import com.example.designmode.glide.utils.Util;

import java.util.HashMap;
import java.util.Map;

//活动缓存
public class ActiveCache {

    private Map<String, Value> map = new HashMap<>();
    private ValueCallback callback;

    public ActiveCache(ValueCallback callback) {this.callback = callback;}

    public void put(String key, Value value) {
        Util.checkNotEmpty(key);

        map.put(key, value);
    }

    public Value get(String key) {
        Value value = map.get(key);
        if (value != null) return value;
        return null;
    }

}
