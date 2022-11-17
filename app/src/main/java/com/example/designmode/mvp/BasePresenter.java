package com.example.designmode.mvp;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

public class BasePresenter<V extends BaseView, M extends BaseModel> {

    private V mView, proxyView;
    private M model;

    public void attach(V view) {
        this.mView = view;
        //动态代理避免每次判断view == null
        proxyView = (V) Proxy.newProxyInstance(view.getClass().getClassLoader(), view.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (mView == null) return null;
                        return method.invoke(mView, args);
                    }
                });
        //反射创建model
        Type supperClass = this.getClass().getGenericSuperclass();
        Type[] typeElement = ((ParameterizedType) supperClass).getActualTypeArguments();
        try {
            model = (M) ((Class)typeElement[1]).newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void dettch() {
        mView = null;
    }

    public V getmView() {
        return proxyView;
    }

    public M getModel() {
        return model;
    }
}
