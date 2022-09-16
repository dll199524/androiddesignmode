package com.example.designmode.performance.start;

import android.content.Context;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class StartUpManager {
    private CountDownLatch mCountDownLatch;
    private Context mContext;
    private List<StartUp<?>> startUpList;
    private StartUpStore startUpStore;

    public StartUpManager(CountDownLatch mCountDownLatch, Context mContext, List<StartUp<?>> startUpList) {
        this.mCountDownLatch = mCountDownLatch;
        this.mContext = mContext;
        this.startUpList = startUpList;
    }

    public StartUpManager start() {
        if (Looper.myLooper() != Looper.getMainLooper())
            throw new RuntimeException("请在主线程调用");
        startUpStore = TopologySort.sort(startUpList);
        for (StartUp<?> startUp : startUpStore.getResult()) {
            StartUpRunnable runnable = new StartUpRunnable(this, mContext, startUp);
            if (startUp.isMainThread())
                runnable.run();
            else
                startUp.executor().execute(runnable);
        }
        return this;
    }

    public void notifyChildren(StartUp<?> startUp) {
        if (!startUp.isMainThread() && startUp.waitMainThread())
            mCountDownLatch.countDown();
        if (startUpStore.getStartUpMap().containsKey(startUp.getClass())) {
            List<Class<? extends StartUp>> childStartUplist =
                    startUpStore.getChildStartUpMap().get(startUp.getClass());
            for (Class<? extends StartUp> childCls : childStartUplist) {
                StartUp<?> childStartUp = startUpStore.getStartUpMap().get(childCls);
                childStartUp.toNotify();
            }

        }
    }

    public void aWait() {
        try {
            mCountDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class Bulider {
        List<StartUp<?>> startUpLists = new ArrayList<>();
        public Bulider addStartUp(StartUp<?> startUp) {
            startUpLists.add(startUp);
            return this;
        }
        public Bulider addAllStartUp(List<StartUp<?>> startUps) {
            startUpLists.addAll(startUps);
            return this;
        }
        public StartUpManager bulider(Context context) {
            AtomicInteger needAwaitCount = new AtomicInteger();
            for (StartUp<?> startUp : startUpLists) {
                if (!startUp.isMainThread() && startUp.waitMainThread())
                    needAwaitCount.incrementAndGet();
            }
            CountDownLatch awaitCountDownLatch = new CountDownLatch(needAwaitCount.get());
            return new StartUpManager(awaitCountDownLatch, context, startUpLists);
        }
    }
}
