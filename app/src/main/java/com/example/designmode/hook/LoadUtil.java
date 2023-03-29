package com.example.designmode.hook;

import android.content.Context;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;
/**在application中初始化
 * 宿主dexElements = 宿主dexElements + 插件dexElements
 * 1.获取宿主dexElements
 * 2.获取插件dexElements
 * 3.合并两个dexElements
 * 4.将新的dexElements 赋值到 宿主dexElements
 * 目标：dexElements  -- DexPathList类的对象 -- BaseDexClassLoader的对象，类加载器
 * 获取的是宿主的类加载器  --- 反射 dexElements  宿主
 * 获取的是插件的类加载器  --- 反射 dexElements  插件
 */
public class LoadUtil {

    private final static String apkPath = "/sdcard/plugin-debug.apk";

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

    public static void load(Context context) {
        try {
            Class clazz = Class.forName("dalvik.system.BaseDexClassLoader");
            Field pathListField = clazz.getField("pathList");


        } catch (Exception e) {e.printStackTrace();}
    }
}
