package com.example.designmode.performance.start;

import android.content.Context;
import android.os.Looper;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class StartUpManager {
    private CountDownLatch mCountDownLatch;
    private Context mContext;
    private List<StartUp<?>> startUpList;

    public StartUpManager(CountDownLatch mCountDownLatch, Context mContext, List<StartUp<?>> startUpList) {
        this.mCountDownLatch = mCountDownLatch;
        this.mContext = mContext;
        this.startUpList = startUpList;
    }

    public StartUpManager start() {
        if (Looper.myLooper() != Looper.getMainLooper())
            throw new RuntimeException("请在主线程调用");
        return null;
    }
}
