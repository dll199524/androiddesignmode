package com.example.designmode.okhttp.interceptor;

import com.example.designmode.okhttp.Request;
import com.example.designmode.okhttp.Response;

import java.io.IOException;

//在缓存可用的情况下，读取本地缓存的数据，如果没有直接去服务器，如果有首先判断有没有缓存策略，
//然后判断有没有过期，如果没有过期直接拿缓存，如果过期了添加之前的一些头信息如：IF-Modifity-Since,
//这个时候后台有可能给你返回304代表你还是可以拿去本地缓存，每次读取到新的响应做一次缓存
public class CacheInterceptor implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        return chain.proceed(request);
    }
}
