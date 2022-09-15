package com.example.designmode.performance.start;

import java.util.concurrent.Executor;

public interface Dispatcher {

    boolean isMainThread();

    boolean waitMainThread();

    void toWait();

    void toNotify();

    Executor executor();

    int getThreadPriority();
}
