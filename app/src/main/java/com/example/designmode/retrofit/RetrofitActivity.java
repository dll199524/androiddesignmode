package com.example.designmode.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.designmode.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        Call<UserLogResult> call = RetrofitClient.getInstance().create().useLogin("long", "111111");
        call.enqueue(new Callback<UserLogResult>() {
            @Override
            public void onResponse(Call<UserLogResult> call, Response<UserLogResult> response) {

            }

            @Override
            public void onFailure(Call<UserLogResult> call, Throwable t) {

            }
        });

        RetrofitClient.getInstance().create().useLogin("long", "111111")
                .enqueue(new HttpCallback() {
                    @Override
                    void onSucceed(Object result) {

                    }

                    @Override
                    void onError(String code, String msg) {

                    }
                });
    }
}