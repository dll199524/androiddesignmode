package com.example.designmode.http;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class HttpUtils {
    private HttpRequest httpRequest;
    private int type;
    private final int TYPE_POST = 0x0001, TYPE_GET = 0x0002;
    private Map<String, Object> params;
    private String url;
    private Context context;
    private boolean cache;

    private HttpUtils(Context context) {
        httpRequest = new HttpRequest();
        params = new HashMap<>();
        this.context = context;
    }

    public HttpUtils with(Context context) {
        this.context = context;
        return this;
    }

    public HttpUtils get() {
        type = TYPE_POST;
        return this;
    }

    public HttpUtils param(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public HttpUtils url(String url) {
        this.url = url;
        return this;
    }

    public HttpUtils cache(boolean cache) {
        this.cache = cache;
        return this;
    }

    public <T> void request(final HttpCallback<T> callback) {

        httpRequest.get(context, url, params, callback, cache);
    }

}
