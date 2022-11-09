package com.example.designmode.okhttp.download;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DownloadTask {

    private String url;
    private long contentLength;
    private DownloadCallback downloadCallback;
    private ExecutorService executorService;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int THREAD_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private int successNum;
    List<DownloadRunnable> runnables;

    public DownloadTask(String url, long contentLength, DownloadCallback downloadCallback) {
        this.url = url;
        this.contentLength = contentLength;
        this.downloadCallback = downloadCallback;
        runnables = new ArrayList<>();
        executorService = executorService();
    }

    public synchronized ExecutorService executorService() {
        executorService = new ThreadPoolExecutor(0, THREAD_SIZE, 30, TimeUnit.SECONDS,
                new SynchronousQueue<>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "runningTask");
                thread.setDaemon(false);
                return thread;
            }
        });
        return executorService;
    }

    public void init() {
        long threadSize = contentLength / THREAD_SIZE;
        for (int i = 0; i < threadSize; i++) {
            //读取数据库
            long start = i * threadSize;
            long end = i + threadSize - 1;
            if (i == threadSize - 1) end = contentLength - 1;
            DownloadRunnable runnable = new DownloadRunnable(start, end, i, url, new DownloadCallback() {
                @Override
                public void onSuccess(File file) {
                    successNum++;
                    if (successNum == threadSize) downloadCallback.onSuccess(file);
                    DownloadDispather.getInstance().recycle(DownloadTask.this);
                    //清楚数据库
                }

                @Override
                public void onFilure(IOException e) {
                    downloadCallback.onFilure(e);
                }
            });
            runnables.add(runnable);
            executorService.execute(runnable);
        }
    }

    public void stop() {
        for (DownloadRunnable runnable : runnables) {
            runnable.stop();
        }
    }
}
