package com.example.designmode.rxjava;

//观察者
public interface Observer<T> {

    void onSubscribe();
    void onNext(T t);
    void onError(Throwable e);
    void onComplete();

}
