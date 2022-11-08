package com.example.designmode.okhttp.interceptor;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

//添加到最后
public class CacheResponseInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        response = response.newBuilder()
                .removeHeader("Cache-Control")
                .addHeader("Cache-Control", "max-age" + 30)
                .build();
        return response;
    }
}
