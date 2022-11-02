package com.example.designmode.okhttp;


import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

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
                URL url = new URL(request.url);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection instanceof HttpsURLConnection) {
                    //https的一些操作
                }
            } catch (Exception e) {e.printStackTrace();
        }
    }
}
