package com.example.designmode.mvp.proxy;

import com.example.designmode.mvp.BaseView;


public class ActivityMvpProxyImpl<V extends BaseView> extends MvpProxyImpl<V> implements ActivityMvpProxy{

    public ActivityMvpProxyImpl(V mView) {
        super(mView);
    }
}
