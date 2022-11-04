package com.example.designmode.okhttp.interceptor;

import com.example.designmode.okhttp.Request;
import com.example.designmode.okhttp.Response;

import java.io.IOException;

//处理重试的拦截器，处理一些异常 不是致命的异常会重新发起请求，致命的异常抛给上一级
//会处理一些重定向如3xx 304 407
public class RetryAndFollowUpInterceptor implements Interceptor{


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        return chain.proceed(request);
    }
}
