package com.example.designmode.glide.binding;

//Application作用域
public class ApplicationLifeCycle implements LifeCycle {
    @Override
    public void addListener(LifeCycleListener lifeCycleListener) {
        lifeCycleListener.onStart();
    }

    @Override
    public void removeListener(LifeCycleListener lifeCycleListener) {

    }
}
