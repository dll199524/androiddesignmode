package com.example.designmode.rxjava;


public class ObservableSchedulers<T> extends Observable{

    final Observable source;
    final Schedulers schedulers;

    public <T>ObservableSchedulers(Observable source, Schedulers schedulers) {
        this.source = source;
        this.schedulers = schedulers;
    }

    @Override
    protected void subscribeActual(Observer observer) {
        schedulers.scheduleDirect(new SchedulersTask(observer));
    }

    private class SchedulersTask implements Runnable{
        final Observer observer;
        public SchedulersTask(Observer observer) {
            this.observer = observer;
        }

        @Override
        public void run() {
            source.subscribe(observer);
        }
    }

}
