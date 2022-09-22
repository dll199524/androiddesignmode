package com.example.designmode.utils;

import android.content.Context;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;

public class HookUtil {

    private final static String apkPath = "";

    public static void loadClass(Context context) {
        try {
            Class<?> clazz = Class.forName("dalvik.system.BaseDexClassLoader");
            Field pathListField = clazz.getDeclaredField("pathList");
            pathListField.setAccessible(true);

            Class<?> dexClazz = Class.forName("dalvik.system.DexPathList");
            Field dexElementField = dexClazz.getField("dexElements");
            dexElementField.setAccessible(true);

            ClassLoader pathClassLoader = context.getClassLoader();
            Object hostPathList = pathListField.get(pathClassLoader);
            Object[] hostDexElement = (Object[]) dexElementField.get(hostPathList);

            ClassLoader dexClassLoader = new DexClassLoader(apkPath, context.getCacheDir().getAbsolutePath(),
                    null, pathClassLoader);
            Object pluginPathList = pathListField.get(dexClassLoader);
            Object[] pluginDexElement = (Object[]) dexElementField.get(dexClassLoader);

            Object[] newDexElement = (Object[]) Array.newInstance(hostDexElement.getClass().getComponentType(),
                    hostDexElement.length + pluginDexElement.length);
            System.arraycopy(hostDexElement, 0, newDexElement, 0, hostDexElement.length);
            System.arraycopy(pluginDexElement, 0, newDexElement, hostDexElement.length, pluginDexElement.length);
            dexElementField.set(hostPathList, newDexElement);

        } catch (Exception e) {e.printStackTrace();}
    }
}
