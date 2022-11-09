package com.example.designmode.okhttp.download;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkhttpManager {

    private volatile static OkhttpManager instance = new OkhttpManager();
    private OkHttpClient okHttpClient;
    public static OkhttpManager getInstance() {return instance;}

    private OkhttpManager() {okHttpClient = new OkHttpClient();}

    public Call asynCall(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        return okHttpClient.newCall(request);
    }

    public Response syncResponse(String url, long start, long end) throws IOException {
        Request request = new Request.Builder()
                .addHeader("Range", "bytes" + start + "--" + end)
                .url(url)
                .build();
        return okHttpClient.newCall(request).execute();
    }

}
