package com.example.designmode.okhttp.interceptor;

import com.example.designmode.okhttp.Request;
import com.example.designmode.okhttp.Response;

import java.io.IOException;

public class ConnectInterceptor implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        return chain.proceed(request);
    }
}
