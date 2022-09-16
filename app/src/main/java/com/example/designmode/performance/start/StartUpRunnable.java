package com.example.designmode.performance.start;

import android.content.Context;
import android.os.Process;

public class StartUpRunnable implements Runnable{
    private StartUpManager startUpManager;
    private Context context;
    private StartUp<?> startUp;

    public StartUpRunnable(StartUpManager startUpManager, Context context, StartUp<?> startUp) {
        this.startUpManager = startUpManager;
        this.context = context;
        this.startUp = startUp;
    }


    @Override
    public void run() {
        Process.setThreadPriority(startUp.getThreadPriority());
        startUp.toWait();
        Object result = startUp.create(context);
        StartUpCacheManager.getInstance().saveInitializedComponet(startUp.getClass(), new Result(result));
        startUpManager.notifyChildren(startUp);
    }
}
