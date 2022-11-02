package com.example.designmode.okhttp;

public interface Call {
    void enqueue(Callback callback);
    Response execute();
}
