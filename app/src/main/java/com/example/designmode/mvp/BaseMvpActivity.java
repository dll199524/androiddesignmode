package com.example.designmode.mvp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.designmode.mvp.proxy.ActivityMvpProxy;
import com.example.designmode.mvp.proxy.ActivityMvpProxyImpl;

public abstract class BaseMvpActivity extends AppCompatActivity implements BaseView{

    ActivityMvpProxy proxy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        initView();
        initData();

        proxy = createActivityProxy();
        proxy.bindCreatePresenter();

    }

    private ActivityMvpProxy createActivityProxy() {
        if (proxy == null) {
            proxy = new ActivityMvpProxyImpl<>(this);
        }
        return proxy;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        proxy.unbinderPresenter();
    }

    protected abstract void setContentView();
    protected abstract void initView();
    protected abstract void initData();
}
