package com.example.designmode.okhttp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.designmode.R;

import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpActivity extends AppCompatActivity {

    private final static String TAG =  OkHttpActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http);
        OkHttpClient client = new OkHttpClient();
        //1.构建一个请求，url端口，请求头的一些参数，表单提交（contentType，contentLength）
        Request request = new Request.Builder()
                .url("http://www.baidu.com")
                .build();
        //2.把request封装成一个RealCall
        Call call = client.newCall(request);
        //3.enqueue队列处理执行
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d(TAG, "onResponse: " + result);
            }
        });
    }
}