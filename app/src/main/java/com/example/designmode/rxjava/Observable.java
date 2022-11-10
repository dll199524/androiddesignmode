package com.example.designmode.rxjava;



public abstract class Observable<T> implements ObservableSource<T> {

    public static <T>Observable<T> just(T item) {return onAssembly(new ObservableJust<T>(item));}
    public static <T>Observable<T> onAssembly(ObservableJust<T> source) {return source;}
    protected abstract void subscribeActual(Observer<? super T> observer);

    @Override
    public void subscribe(Observer<T> observer) {
        subscribeActual(observer);
    }

    public void subscribe(Consumer<T> onNext) {subscribe(onNext, null, null);}
    public void subscribe(Consumer<T> onNext, Consumer<T> error, Consumer<T> complete) {subscribe(new LambdaObserver(onNext));}
}
