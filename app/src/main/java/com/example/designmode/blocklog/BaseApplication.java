package com.example.designmode.blocklog;

import android.app.Application;
import android.os.Looper;

public class BaseApplication extends Application {
    LogPinter logPinter;
    @Override
    public void onCreate() {
        super.onCreate();
        logPinter = new LogPinter(this);
        Looper.getMainLooper().setMessageLogging(logPinter);
    }
}
