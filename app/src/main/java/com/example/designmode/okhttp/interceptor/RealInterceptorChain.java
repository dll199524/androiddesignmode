package com.example.designmode.okhttp.interceptor;

import com.example.designmode.okhttp.Request;

import java.io.IOException;
import java.util.List;

public class RealInterceptorChain implements Interceptor.Chain{

    Request request;
    List<Interceptor> interceptors;

    public RealInterceptorChain(Request request, List<Interceptor> interceptors) {
        this.request = request;
        this.interceptors = interceptors;
    }

    @Override
    public Request request() {
        return request;
    }

    @Override
    public void proceed(Request request) throws IOException {

    }
}
