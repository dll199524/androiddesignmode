package com.example.designmode.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ShareReflectUtil {


    public static Field findField(Object instance, String name) throws NoSuchFieldException {
        for (Class<?> clazz = instance.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Field field = clazz.getField(name);
                if (!field.isAccessible()) field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        throw new NoSuchFieldException("Field" + name + "not find in" + instance.getClass());
    }

    public static Method findMethod(Object instance, String name, Class<?>... parameterTypes) throws NoSuchMethodException {
        for (Class<?> clazz = instance.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Method method = clazz.getMethod(name, parameterTypes);
                if (!method.isAccessible()) method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        throw new NoSuchMethodException("Method "
                + name
                + " with parameters "
                + Arrays.asList(parameterTypes)
                + " not found in " + instance.getClass());
    }

    public static Object[] combineElements(Object instance, String fileName, Object[] patchElements) throws Exception {
        Field oldField = findField(instance, fileName);
        Object[] oldElements = (Object[]) oldField.get(instance);
        Object[] newElements = (Object[]) Array.newInstance(oldElements.getClass().getComponentType(),
                oldElements.length + patchElements.length);
        System.arraycopy(patchElements, 0, newElements, 0, patchElements.length);
        System.arraycopy(oldElements, 0, newElements, patchElements.length, oldElements.length);
        return newElements;
    }
}
