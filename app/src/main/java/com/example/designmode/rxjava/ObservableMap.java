package com.example.designmode.rxjava;


public class ObservableMap<T, R> extends Observable<R>{

    private Observable source;
    private Function<T, R> function;

    public ObservableMap(Observable source, Function<T, R> function) {
        this.source = source;
        this.function = function;
    }

    @Override
    protected void subscribeActual(Observer<? super R> observer) {
        source.subscribe(new MapObserver(observer, function));
    }

    class MapObserver<T> implements Observer<T> {
        final Observer<? super R> source;
        final Function<T, R> function;

        public MapObserver(Observer<? super R> source, Function<T, R> function) {
            this.source = source;
            this.function = function;
        }

        @Override
        public void onSubscribe() {
            source.onSubscribe();
        }

        @Override
        public void onNext(T t) {
            R apply = function.apply(t);
            source.onNext(apply);
        }

        @Override
        public void onError(Throwable e) {
            source.onError(e);
        }

        @Override
        public void onComplete() {
            source.onComplete();
        }
    }
}
