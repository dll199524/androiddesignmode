package com.example.designmode.okhttp.interceptor;

import com.example.designmode.okhttp.RealCall;
import com.example.designmode.okhttp.Request;
import com.example.designmode.okhttp.RequestBody;
import com.example.designmode.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

//处理连接
//写数据和读取数据，写头部信息，写body表单信息等
public class CallServerInterceptor implements Interceptor{

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        URL url = new URL(request.getUrl());
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        if (urlConnection instanceof HttpsURLConnection) {
            //https的一些操作
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) urlConnection;
//            httpsURLConnection.setHostnameVerifier();
//            httpsURLConnection.setSSLSocketFactory();
        }
//        urlConnection.setReadTimeout();
        //写东西
        urlConnection.setRequestMethod(request.getMethod().name);
        urlConnection.setDoOutput(request.getMethod().doOutput());
        //头信息
        RequestBody requestBody = request.getRequestBody();
        if (requestBody != null) {
            urlConnection.setRequestProperty("Content-Type", requestBody.getContentType());
            urlConnection.setRequestProperty("Content-Length", Long.toString(requestBody.getContentLength()));
        }
        //写内容
        if (requestBody != null) {
            requestBody.onWriteBody(urlConnection.getOutputStream());
        }


        urlConnection.connect();


        int statusCode = urlConnection.getResponseCode();
        if (statusCode == 200) {
            InputStream is = urlConnection.getInputStream();
            Response response = new Response(is);
            return response;
        }
        return chain.proceed(request);
    }
}
