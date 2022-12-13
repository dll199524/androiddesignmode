package com.example.designmode.glide.core;

import android.content.Context;

import com.example.designmode.glide.binding.LifeCycle;
import com.example.designmode.glide.binding.LifeCycleListener;

public class RequestManager implements LifeCycleListener {

    private LifeCycle lifeCycle;

    public RequestManager(Glide glide, LifeCycle lifeCycle, Context applicationContext) {
        this.lifeCycle = lifeCycle;
        lifeCycle.addListener(this);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onStop() {

    }
}
