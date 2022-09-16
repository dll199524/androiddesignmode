package com.example.designmode.performance.start;

import android.os.Process;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

public abstract class AndroidStartUp<T> implements StartUp<T>{

    private CountDownLatch mWaitCountDownLatch = new CountDownLatch(getDependiceCount());

    @Override
    public int getDependiceCount() {
        List<Class<? extends StartUp<?>>> dependencies = dependencies();
        return dependencies == null ? 0 : dependencies.size();
    }

    @Override
    public void toWait() {
        try {
            mWaitCountDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toNotify() {
        mWaitCountDownLatch.countDown();
    }

    @Override
    public Executor executor() {
        return ExecutorManager.ioExecutor;
    }

    @Override
    public int getThreadPriority() {
        return Process.THREAD_PRIORITY_DEFAULT;
    }
}
