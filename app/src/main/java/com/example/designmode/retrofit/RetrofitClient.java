package com.example.designmode.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    Retrofit retrofit;
    OkHttpClient client = new OkHttpClient();

    //1.没打印
    //2.数据格式不一致 成功data是个对象 不成功data是个string
    //3.baseurl的问题

    public static RetrofitClient getInstance() {
        return RetrofitHolder.instance;
    }

    private Retrofit getRetrofit() {
        return retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }


    static class RetrofitHolder {
        static RetrofitClient instance = new RetrofitClient();
    }

    public ServiceApi create() {return getRetrofit().create(ServiceApi.class);}
}
