package com.example.designmode.okhttp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.designmode.R;

import java.io.IOException;

public class OkHttpTestActivity extends AppCompatActivity {

    private final static String TAG =  OkHttpTestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http_test);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Bulider()
                .bulid();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: " + response.string());
            }
        });
    }
}