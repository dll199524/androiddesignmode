package com.example.designmode.retrofit.source;

import java.io.IOException;

import okhttp3.Response;

public class OkHttpCall<T> implements Call<T>{
    ServiceMethod serviceMethod;
    Object[] args;

    public OkHttpCall(ServiceMethod serviceMethod, Object[] args) {
        this.serviceMethod = serviceMethod;
        this.args = args;
    }

    @Override
    public void enqueue(Callback<T> callback) {
        okhttp3.Call call = serviceMethod.createNewCall(args);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                if (callback != null) callback.onFailure(OkHttpCall.this, e);
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                com.example.designmode.retrofit.source.Response response1 = new com.example.designmode.retrofit.source.Response();
                response1.body = serviceMethod.parseBody(response.body());
                if (callback != null) callback.onResponse(OkHttpCall.this, response1);
            }
        });
    }
}
