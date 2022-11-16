package com.example.designmode.retrofit.source;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.OkHttpClient;

public class Retrofit {

    final String baseUrl;
    final Call.Factory factory;
    private Map<Method, ServiceMethod> serviceMethodMap = new ConcurrentHashMap<>();

    public Retrofit(Bulider bulider) {
        this.baseUrl = bulider.baseUrl;
        this.factory = bulider.callFactory;
    }

    public <T> T create(final Class<T> service)  {
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Log.d("TAG", "invoke: " + method.getName());
                        //是不是object方法
                        if (method.getDeclaringClass() == Object.class)
                            method.invoke(proxy, args);
                        //解析注解参数
                        ServiceMethod serviceMethod = loadServiceMethod(method);
                        //封装OkHttpCall
                        OkHttpCall call = new OkHttpCall(serviceMethod, args);
                        return call;
                    }
                });
    }

    private ServiceMethod loadServiceMethod(Method method) {
        ServiceMethod serviceMethod = serviceMethodMap.get(method);
        if (serviceMethod == null) {
            serviceMethod = new ServiceMethod.Builder(this, method).build();
            serviceMethodMap.put(method, serviceMethod);
        }
        return serviceMethod;
    }

    static class Bulider {
        String baseUrl;
        Call.Factory callFactory;

        public Bulider url(String url) {
            baseUrl = url;
            return this;
        }

        public Bulider client(Call.Factory callFactory) {
            this.callFactory = callFactory;
            return this;
        }

        public Retrofit bulid() {
            if (callFactory == null)
                callFactory = new OkHttpClient();
            return new Retrofit(this);
        }
    }
}
