
package com.example.designmode.eventBus;


import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {

   public static String TAG = "EventBus";

    static volatile EventBus defaultInstance;
    private final Map<Class<?>, CopyOnWriteArrayList<Subscription>> subscriptionsByEventType;
    private final Map<Object, List<Class<?>>> typesBySubscriber;

    public EventBus() {
        subscriptionsByEventType = new HashMap<>();
        typesBySubscriber = new HashMap<>();
    }

    public static EventBus getDefault() {
        EventBus instance = defaultInstance;
        if (instance == null) {
            synchronized (EventBus.class) {
                instance = EventBus.defaultInstance;
                if (instance == null) {
                    instance = EventBus.defaultInstance = new EventBus();
                }
            }
        }
        return instance;
    }

    public void register(Object object) {
        //1.解析所有方法封装成SubscriberMethod
        List<SubscriberMethod> subscriberMethods = new ArrayList<>();
        Class<?> objClazz = object.getClass();
        Method[] methods = objClazz.getDeclaredMethods();
        for (Method method : methods) {
            Subscribe subscribe = method.getAnnotation(Subscribe.class);
            if (subscribe != null) {
                Class<?>[] parameterType = method.getParameterTypes();//方法类型
                if (parameterType.length != 1) throw new RuntimeException(method.getName() + "方法只支持一个参数");
                SubscriberMethod subscriberMethod = new SubscriberMethod(method, parameterType[0], subscribe.threadMode(),
                         subscribe.priority(), subscribe.stiky());
                subscriberMethods.add(subscriberMethod);
            }
        }
        //2.按照规则存放到map里面去subscriptionsByEventType
        for (SubscriberMethod subscriberMethod : subscriberMethods) {
            subscribe(object, subscriberMethod);
        }
    }

    public void subscribe(Object obj, SubscriberMethod subscriberMethod) {
        Class<?> eventType = subscriberMethod.eventType;
        CopyOnWriteArrayList<Subscription> subscriptions = subscriptionsByEventType.get(eventType);
        if (subscriptions == null) {
            subscriptions = new CopyOnWriteArrayList<>();
            subscriptionsByEventType.put(eventType, subscriptions);
        }

        //判断优先级不写
        Subscription subscription = new Subscription(obj, subscriberMethod);
        subscriptions.add(subscription);

        //typesBySubscriber方便移除
        List<Class<?>> eventTypes = typesBySubscriber.get(obj);
        if (eventTypes == null) {
            eventTypes = new ArrayList<>();
            eventTypes.add(eventType);
        }
        if (!eventTypes.contains(eventType))
            eventTypes.add(eventType);
    }

    public void post(Object obj) {
        //遍历subscriptionByEventType，找到符合的方法调用方法的method.invoke()执行 注意线程切换
        Class<?> eventType = obj.getClass();
        CopyOnWriteArrayList<Subscription> subscriptions = subscriptionsByEventType.get(eventType);
        if (subscriptions != null) {
            for (Subscription subscription : subscriptions) {
                executeMethod(subscription, obj);
            }
        }
    }

    private void executeMethod(Subscription subscription, Object obj) {
        ThreadMode threadMode = subscription.subscriberMethod.threadMode;
        boolean isMainThread = Looper.getMainLooper() == Looper.myLooper();
        switch (threadMode) {
            case POSTING:
                invokeMethod(subscription, obj);
                break;
            case MAIN:
                if (isMainThread) {invokeMethod(subscription, obj);}
                else {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            invokeMethod(subscription, obj);
                        }
                    });
                }
                break;
            case BACKGROUND:
                if (!isMainThread) invokeMethod(subscription, obj);
                else {AsyncPoster.enqueue(subscription, obj);}
                break;
            case ASYNC:
                AsyncPoster.enqueue(subscription, obj);
                break;
        }
    }

    private void invokeMethod(Subscription subscription, Object obj) {
        try {
            subscription.subscriberMethod.method.invoke(subscription.subscriber, obj);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void ungister(Object obj) {
        List<Class<?>> eventTypes = typesBySubscriber.get(obj);
        if (eventTypes != null) {
            for (Class<?> eventType : eventTypes) {
                removeObj(obj, eventType);
            }
        }
    }

    private void removeObj(Object obj, Class<?> eventType) {
        List<Subscription> subscriptions = subscriptionsByEventType.get(eventType);
        if (subscriptions != null) {
            int size = subscriptions.size();
            for (int i = 0; i < size; i++) {
                Subscription subscription = subscriptions.get(i);
                if (subscription.subscriber == obj) {
                    subscriptions.remove(i);
                    i--;
                    size--;
                }
            }
        }
    }
}