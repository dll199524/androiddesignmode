package com.example.designmode.rxjava;


public class ObservableJust<T> extends Observable<T>{

    T item;

    public ObservableJust(T item) {
        this.item = item;
    }


    @Override
    protected void subscribeActual(Observer observer) {
        ScalarDisposable<T> sd = new ScalarDisposable<>(observer, item);
        observer.onSubscribe();
        sd.run();
    }

    class ScalarDisposable<T> {
        Observer observer;
        T item;

        public ScalarDisposable(Observer observer, T item) {
            this.observer = observer;
            this.item = item;
        }

        void run() {
            try {
                observer.onNext(item);
                observer.onComplete();
            } catch (Exception e) {observer.onError(e);}
        }
    }

}
