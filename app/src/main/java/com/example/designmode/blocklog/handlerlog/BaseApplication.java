package com.example.designmode.blocklog.handlerlog;

import android.app.Application;
import android.os.Looper;

import com.example.designmode.blocklog.ExpectionCrashHandler;

public class BaseApplication extends Application {
    LogPinter logPinter;
    @Override
    public void onCreate() {
        super.onCreate();
        logPinter = new LogPinter(this);
        Looper.getMainLooper().setMessageLogging(logPinter);
        ExpectionCrashHandler.getInstance().init(this);
    }
}
