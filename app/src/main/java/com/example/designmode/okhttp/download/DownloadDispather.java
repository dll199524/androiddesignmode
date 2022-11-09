package com.example.designmode.okhttp.download;


import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DownloadDispather {

    private static volatile DownloadDispather instance = new DownloadDispather();
    private Deque<DownloadTask> readyTask = new ArrayDeque<>(20);
    private Deque<DownloadTask> runningTask = new ArrayDeque<>(20);
    private Deque<DownloadTask> finishTask = new ArrayDeque<>(20);

    public static DownloadDispather getInstance() {return instance;}
    private DownloadDispather() {}

    public void startDownload(String url, final DownloadCallback callback) {
        Call call = OkhttpManager.getInstance().asynCall(url);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFilure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                long contentLength = response.body().contentLength();
                if (contentLength <= -1) {
                    //....
                    return;
                } else {
                    DownloadTask downloadTask = new DownloadTask(url, contentLength, callback);
                    downloadTask.init();
                    runningTask.add(downloadTask);
                }

            }
        });
    }

    public void stopDownLoad(String url) {
        //
    }


    public void recycle(DownloadTask downloadTask) {
        runningTask.remove(downloadTask);
    }

}
