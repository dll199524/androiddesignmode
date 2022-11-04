package com.example.designmode.okhttp.interceptor;

import com.example.designmode.okhttp.Request;
import com.example.designmode.okhttp.Response;

import java.io.IOException;
import java.util.List;

public class RealInterceptorChain implements Interceptor.Chain{

    int index;
    List<Interceptor> interceptors;
    Request request;

    public RealInterceptorChain(int index, List<Interceptor> interceptors, Request request) {
        this.index = index;
        this.interceptors = interceptors;
        this.request = request;
    }

    @Override
    public Request request() {return request;}

    @Override
    public Response proceed(Request request) throws IOException {
        RealInterceptorChain next = new RealInterceptorChain(index + 1, interceptors, request);
        Interceptor interceptor = interceptors.get(index);
        Response response = interceptor.intercept(next);
        return response;
    }
}
