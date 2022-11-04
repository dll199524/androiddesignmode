package com.example.designmode.okhttp;


import android.util.Log;

import com.example.designmode.okhttp.interceptor.BridgeInterceptor;
import com.example.designmode.okhttp.interceptor.CacheInterceptor;
import com.example.designmode.okhttp.interceptor.ConnectInterceptor;
import com.example.designmode.okhttp.interceptor.Interceptor;
import com.example.designmode.okhttp.interceptor.RealInterceptorChain;
import com.example.designmode.okhttp.interceptor.RetryAndFollowUpInterceptor;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class RealCall implements Call {

    private final static String TAG = "RealCall";
    private final Request orignalRequest;
    private final OkHttpClient okHttpClient;

    public RealCall(Request request, OkHttpClient client) {
        this.orignalRequest = request;
        this.okHttpClient = client;
    }

    @Override
    public void enqueue(Callback callback) {
        okHttpClient.dispatcher().enqueue(new AsyncCall(callback));
    }

    @Override
    public Response execute() {
        return null;
    }

    final class AsyncCall extends NamedRunnable {
        private Callback callback;
        public AsyncCall(Callback callback) {
            this.callback = callback;
        }

        @Override
        protected void execute() {
            //开始访问网络Request -> Response
            Log.d(TAG, "execute: -------");
            //Volley xUtils 基于HttpUrlConnection  okhttp 基于 socket + okio
            final Request request = orignalRequest;
            try {
//                URL url = new URL(request.url);
//
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                if (urlConnection instanceof HttpsURLConnection) {
                    //https的一些操作
//                    HttpsURLConnection httpsURLConnection = (HttpsURLConnection) urlConnection;
//                    httpsURLConnection.setHostnameVerifier();
//                    httpsURLConnection.setSSLSocketFactory();
//                }
//                urlConnection.setReadTimeout();
                //写东西
//                urlConnection.setRequestMethod(request.method.name);
//                urlConnection.setDoOutput(request.method.doOutput());
//                //头信息
//                RequestBody requestBody = request.requestBody;
//                if (requestBody != null) {
//                    urlConnection.setRequestProperty("Content-Type", requestBody.getContentType());
//                    urlConnection.setRequestProperty("Content-Length", Long.toString(requestBody.getContentLength()));
//                }
//                //写内容
//                if (requestBody != null) {
//                    requestBody.onWriteBody(urlConnection.getOutputStream());
//                }
//
//
//                urlConnection.connect();
//
//
//                int statusCode = urlConnection.getResponseCode();
//                if (statusCode == 200) {
//                    InputStream is = urlConnection.getInputStream();
//                    Response response = new Response(is);
//                    callback.onResponse(RealCall.this, response);
//                }

                List<Interceptor> interceptors = new ArrayList<>();
                interceptors.add(new RetryAndFollowUpInterceptor());
                interceptors.add(new BridgeInterceptor());
                interceptors.add(new CacheInterceptor());
                interceptors.add(new ConnectInterceptor());
                RealInterceptorChain chain = new RealInterceptorChain(0, interceptors, orignalRequest);
                Response response = chain.proceed(orignalRequest);
                callback.onResponse(RealCall.this, response);
            } catch (Exception e) {e.printStackTrace();}


        }
    }
}
