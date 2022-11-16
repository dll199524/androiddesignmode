package com.example.designmode.retrofit.source;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class RequestBulider {

    ParameterHandler<Object>[] parameterHandlers;
    Object[] args;
    HttpUrl.Builder httpUrl;

    public RequestBulider(String baseUrl, String relativeUrl, ParameterHandler<?>[] parameterHandlers, Object[] args) {
        this.parameterHandlers = (ParameterHandler<Object>[]) parameterHandlers;
        this.args = args;
        httpUrl = HttpUrl.parse(baseUrl + relativeUrl).newBuilder();
    }

    public Request bulid() {
        int size = parameterHandlers.length;
        for (int i = 0; i < size; i++) {
            parameterHandlers[i].apply(this, args);
        }
        Request request = new Request.Builder()
                .url(httpUrl.build())
                .build();
        return request;
    }

    public void addQuerryName(String key, String val) {
        httpUrl.addQueryParameter(key, val);
    }
}
