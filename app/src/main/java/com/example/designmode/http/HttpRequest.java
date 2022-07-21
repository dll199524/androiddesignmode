package com.example.designmode.http;

import android.content.Context;

import java.util.Map;

public class HttpRequest {
    private HttpCache httpCache;

    public HttpRequest() {
        httpCache = new HttpCache();
    }

    public <T> void get(Context context, String url, Map<String, Object> params, HttpCallback<T> callback, boolean cache) {
    }
}
