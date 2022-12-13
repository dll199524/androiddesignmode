package com.example.designmode.glide.binding;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

//非Application作用域
public class ActivityFragmentLifeCycle implements LifeCycle {

    private final Set<LifeCycleListener> lifeCycleListeners =
            Collections.newSetFromMap(new WeakHashMap<LifeCycleListener, Boolean>());
    boolean isStart, isDestory;

    @Override
    public void addListener(LifeCycleListener lifeCycleListener) {
        lifeCycleListeners.add(lifeCycleListener);
        if (isDestory) lifeCycleListener.onDestroy();
        else if (isStart) lifeCycleListener.onStart();
        else lifeCycleListener.onStop();
    }

    @Override
    public void removeListener(LifeCycleListener lifeCycleListener) {
        lifeCycleListeners.remove(lifeCycleListener);
    }

    void onStrat() {
        isStart = true;
        for (LifeCycleListener lifeCycleListener : lifeCycleListeners) {
            lifeCycleListener.onStart();
        }
    }

    void onDestroy() {
        isDestory = true;
        for (LifeCycleListener lifeCycleListener : lifeCycleListeners) {
            lifeCycleListener.onDestroy();
        }
    }

    void onStop() {
        isStart = false;
        for (LifeCycleListener lifeCycleListener : lifeCycleListeners) {
            lifeCycleListener.onStop();
        }
    }
}
