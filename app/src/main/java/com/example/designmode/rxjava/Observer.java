package com.example.designmode.rxjava;

public interface Observer<T> {

    void onSubscribe();
    void onNext(T t);
    void onError(Throwable e);
    void onComplete();

}
