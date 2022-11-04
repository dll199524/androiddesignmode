package com.example.designmode.okhttp.interceptor;

import com.example.designmode.okhttp.Request;
import com.example.designmode.okhttp.RequestBody;
import com.example.designmode.okhttp.Response;

import java.io.IOException;

//处理一些通用的请求头：Content-Type Connection Content-Length Cookie
//做一些返回的处理如果返回的数据压缩了采用zipSource，保存Cookie
public class BridgeInterceptor implements Interceptor{

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request.headers("Connection", "keep-alive");
        RequestBody requestBody = request.getRequestBody();
        if (requestBody != null) {
            request.headers("Content-Type", requestBody.getContentType());
            request.headers("Content-Length", Long.toString(requestBody.getContentLength()));
        }
        return chain.proceed(request);
    }
}
