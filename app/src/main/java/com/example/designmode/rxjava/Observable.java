package com.example.designmode.rxjava;


//被观察者 观察者模式 静态代理等等
public abstract class Observable<T> implements ObservableSource<T> {

    public static <T> Observable<T> just(T item) {return onAssembly(new ObservableJust<T>(item));}
    public static <T> Observable<T> onAssembly(Observable<T> source) {
        return source;
    }
    public <R> Observable<R> map(Function<T, R> function) {
        return onAssembly(new ObservableMap<>(this, function));
    }


    protected abstract void subscribeActual(Observer<? super T> observer);

    @Override
    public void subscribe(Observer<T> observer) {
        subscribeActual(observer);
    }

    public void subscribe(Consumer<T> onNext) {subscribe(onNext, null, null);}
    public void subscribe(Consumer<T> onNext, Consumer<T> error, Consumer<T> complete) {subscribe(new LambdaObserver(onNext));}
    public Observable<T> subscribeOn(Schedulers schedulers) {return onAssembly(new ObservableSchedulers<T>(this, schedulers));}
    public Observable<T> observeOn(Schedulers schedulers) {return onAssembly(new ObservableSchedulers<T>(this, schedulers));}
}
