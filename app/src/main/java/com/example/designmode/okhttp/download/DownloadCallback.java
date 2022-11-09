package com.example.designmode.okhttp.download;

import java.io.File;
import java.io.IOException;

public interface DownloadCallback {

    void onSuccess(File file);
    void onFilure(IOException e);
}
