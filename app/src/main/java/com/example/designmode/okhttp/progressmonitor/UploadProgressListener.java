package com.example.designmode.okhttp.progressmonitor;

public interface UploadProgressListener {
    void onProgress(long current, long total);
}
