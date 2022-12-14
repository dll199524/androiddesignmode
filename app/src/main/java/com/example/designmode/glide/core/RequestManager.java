package com.example.designmode.glide.core;

import android.content.Context;

import com.example.designmode.glide.binding.LifeCycle;
import com.example.designmode.glide.binding.LifeCycleCallBack;
import com.example.designmode.glide.binding.LifeCycleListener;

public class RequestManager implements LifeCycleListener {

    private LifeCycle lifeCycle;
    private LifeCycleCallBack callBack;

    public RequestManager(Glide glide, LifeCycle lifeCycle, LifeCycleCallBack callBack, Context applicationContext) {
        this.lifeCycle = lifeCycle;
        this.callBack = callBack;
        lifeCycle.addListener(this);
    }

    @Override
    public void onStart() {
        callBack.initAction();
    }

    @Override
    public void onDestroy() {
        callBack.stopAction();
    }

    @Override
    public void onStop() {
        callBack.recycleAction();
    }
}
