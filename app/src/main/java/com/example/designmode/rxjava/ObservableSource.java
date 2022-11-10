package com.example.designmode.rxjava;

public interface ObservableSource<T> {

    void subscribe(Observer<T> observer);
}
