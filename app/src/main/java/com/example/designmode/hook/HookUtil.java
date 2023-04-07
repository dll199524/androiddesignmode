package com.example.designmode.hook;


import android.app.ActivityManager;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

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
        try {
            // 获取 ActivityThread 类的 Class 对象
            Class<?> clazz = Class.forName("android.app.ActivityThread");
            // 获取 ActivityThread 对象
            Field activityThreadField = clazz.getDeclaredField("sCurrentActivityThread");
            activityThreadField.setAccessible(true);
            Object activityThread = activityThreadField.get(null);

            Field mHField = clazz.getDeclaredField("mH");
            mHField.setAccessible(true);
            final Handler mH = (Handler) mHField.get(activityThread);

            Field mCallbackField = Handler.class.getDeclaredField("mCallback");
            mCallbackField.setAccessible(true);

            Handler.Callback mCallback = new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    switch (msg.what) {
                        //8.0之前
                        case 100:
                            try {
                                Field intentField = msg.obj.getClass().getDeclaredField("intent");
                                intentField.setAccessible(true);
                                Intent proxyIntent = (Intent) intentField.get(msg.obj);
                                Intent intent = proxyIntent.getParcelableExtra(TARGET_INTENT);
                                if (intent != null) intentField.set(msg.obj, intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        //9.0之后
                        //private Intent mIntent; -- LaunchActivityItem 对象  -- private List<ClientTransactionItem> mActivityCallbacks;
                        //		-- ClientTransaction == msg.obj
                        case 159:
                            try {
                                Field mActivityCallbacksField = msg.obj.getClass().getDeclaredField("mActivityCallbacks");
                                mActivityCallbacksField.setAccessible(true);
                                List mActivityCallbacks = (List) mActivityCallbacksField.get(msg.what);
                                for (int i = 0; i < mActivityCallbacks.size(); i++) {
                                    if (mActivityCallbacks.get(i).equals("android.app.servertransaction.LaunchActivityItem")) {
                                        Object launchActivityItem = mActivityCallbacks.get(i);
                                        Field intentField = launchActivityItem.getClass().getDeclaredField("mIntent");
                                        intentField.setAccessible(true);
                                        Intent mProxyIntent = (Intent) intentField.get(launchActivityItem);

                                        Intent intent = mProxyIntent.getParcelableExtra(TARGET_INTENT);
                                        if (intent != null) intentField.set(launchActivityItem, intent);
                                    }

                                }
                            } catch (Exception e) {e.printStackTrace();}

                    }
                    return false;
                }
            };
            mCallbackField.set(mH, mCallback);

        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
