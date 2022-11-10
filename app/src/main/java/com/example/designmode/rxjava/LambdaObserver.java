package com.example.designmode.rxjava;

public class LambdaObserver<T> implements Observer<T> {

    Consumer onNext;
    public LambdaObserver(Consumer onNext) {
        this.onNext = onNext;
    }

    @Override
    public void onSubscribe() {

    }

    @Override
    public void onNext(T item) {
        onNext.onNext(item);
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
