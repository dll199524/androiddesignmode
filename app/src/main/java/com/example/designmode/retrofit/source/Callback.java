package com.example.designmode.retrofit.source;


public interface Callback<T> {

    void onResponse(Call<T> call, Response<T> response);
    void onFailure(Call<T> call, Throwable e);
}
