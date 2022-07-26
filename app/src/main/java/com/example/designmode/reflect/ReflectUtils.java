package com.example.designmode.reflect;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ReflectUtils {

    public static void injectSetContentView(Activity activity) {
        if (activity == null) return;
        Class<? extends Activity> clazz = activity.getClass();
        InjectSetContentView annotation = clazz.getAnnotation(InjectSetContentView.class);
        if (annotation != null) {
            int val = annotation.value();
            try {
                Method setContentViewMethod = clazz.getMethod("setContentView", int.class);
                setContentViewMethod.setAccessible(true);
                setContentViewMethod.invoke(activity, val);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static void injectView(Activity activity) {
        if (activity == null) return;
        Class<? extends Activity> clazz = activity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(InjectView.class)) {
                InjectView annotation = field.getAnnotation(InjectView.class);
                if (annotation != null) {
                    int val = annotation.value();
                    View view = activity.findViewById(val);
                    field.setAccessible(true);
                    try {
                        field.set(activity, view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void injectOnClick(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            InjectOnclick annotation = method.getAnnotation(InjectOnclick.class);
            if (annotation != null) {
                int[] val = annotation.value();
                method.setAccessible(true);
                Object listener = Proxy.newProxyInstance(View.OnClickListener.class.getClassLoader(),
                        new Class[]{View.OnClickListener.class}, new InvocationHandler() {
                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                return method.invoke(activity, args);
                            }
                        });
                for (int id : val) {
                    View v = activity.findViewById(id);
                    try {
                        Method setClickListener = v.getClass().getMethod("setOnClickListener", View.OnClickListener.class);
                        setClickListener.invoke(v, listener);
                    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
