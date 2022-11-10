package com.example.designmode.rxjava;



public abstract class Observable<T> implements ObservableSource<T> {

    public static <T>Observable<T> just(T item) {return onAssembly(new ObservableJust<T>(item));}

    public static <T>Observable<T> onAssembly(ObservableJust<T> source) {return source;}

    protected abstract void subscribeActual(Observer<? super T> observer);

    @Override
    public void subscribe(Observer<T> observer) {
        subscribeActual(observer);
    }
}
