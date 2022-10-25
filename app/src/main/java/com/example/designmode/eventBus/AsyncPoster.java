package com.example.designmode.eventBus;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncPoster implements Runnable {

    Subscription subscription;
    Object obj;
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    public AsyncPoster(Subscription subscription, Object obj) {
        this.subscription = subscription;
        this.obj = obj;
    }

    public static void enqueue(Subscription subscription, Object obj) {
        executorService.execute(new AsyncPoster(subscription, obj));
    }


    @Override
    public void run() {
        try {
            subscription.subscriberMethod.method.invoke(subscription.subscriber, obj);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
