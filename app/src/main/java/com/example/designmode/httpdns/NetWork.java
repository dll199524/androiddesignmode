package com.example.designmode.httpdns;

import com.example.designmode.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetWork {
    private static volatile NetWork instance;
    private Retrofit retrofit;

    public static NetWork getInstance() {
        if (instance == null) {
            synchronized (NetWork.class) {
                if (instance == null)
                    instance = new NetWork();
            }
        }
        return instance;
    }

    public Retrofit getRetrofit() {
        if (retrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .dns(OkHttpDns.getInstance())
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS);
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(interceptor);
            }
            retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .baseUrl(Api.BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public Api getApiService() {
        return getRetrofit().create(Api.class);
    }
}
