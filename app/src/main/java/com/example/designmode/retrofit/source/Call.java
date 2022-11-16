package com.example.designmode.retrofit.source;

public interface Call<T> {

    void enqueue(Callback<T> callback);
}
