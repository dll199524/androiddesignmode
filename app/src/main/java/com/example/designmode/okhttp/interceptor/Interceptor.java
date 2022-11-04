package com.example.designmode.okhttp.interceptor;

import com.example.designmode.okhttp.Request;
import com.example.designmode.okhttp.Response;

import java.io.IOException;

public interface Interceptor {

    Response intercept(Chain chain) throws IOException;

    interface Chain {
        Request request();
        Response proceed(Request request) throws IOException;
    }
}
