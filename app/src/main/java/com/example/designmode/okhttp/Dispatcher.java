package com.example.designmode.okhttp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class Dispatcher {

    private ExecutorService executorService;

    public synchronized ExecutorService executorService() {
        executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(false);
                return thread;
            }
        });
        return executorService;
    }

    public void enqueue(RealCall.AsyncCall asyncCall) {
        executorService().execute(asyncCall);
    }
}
