package com.example.designmode.hook;


import android.content.Intent;
import android.os.Build;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class HookUtil {

    private static final String TARGET_INTENT = "target_intent";
    public static void hookAMS() {
        try {
            Field singletonField = null;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) { // 小于8.0
                Class<?> clazz = Class.forName("android.app.ActivityManagerNative");
                singletonField = clazz.getDeclaredField("gDefault");
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
                Class<?> clazz = Class.forName("android.app.ActivityManager");
                singletonField = clazz.getDeclaredField("IActivityManagerSingleton");
            } else {
                Class<?> clazz = Class.forName("android.app.ActivityTaskManager");
                singletonField = clazz.getDeclaredField("IActivityTaskManagerSingleton");
            }
            singletonField.setAccessible(true);
            Object singleton = singletonField.get(null);

            Class<?> singletonClass = Class.forName("android.util.Singleton");
            Field mInstanceField = singletonClass.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);
            final Object mInstance = mInstanceField.get(singleton);

            Class<?> iActivityClass = null;

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
                iActivityClass = Class.forName("android.app.IActivityManager");
            } else {
                iActivityClass = Class.forName("android.app.ActivityTaskManager");
            }

            Object proxyInstance = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    new Class[]{iActivityClass}, new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            if (method.getName().equals("startActivity")) {
                                int index = -1;
                                for (int i = 0; i < args.length; i++) {
                                    if (args[i] instanceof Intent) {
                                        index = i;
                                        break;
                                    }
                                }
                                Intent in = (Intent) args[index];
                                Intent proxyIntent = new Intent();
                                proxyIntent.setClassName("com.example.designmode",
                                        "com.example.designmode.ProxyActivity");
                                proxyIntent.putExtra(TARGET_INTENT, in);
                                args[index] = proxyIntent;
                            }

                            return method.invoke(mInstance, args);
                        }
                    });
            singletonField.set(mInstance, proxyInstance);

        } catch (Exception e) {e.printStackTrace();}
    }

    public static void hookHandler() {

    }
}
