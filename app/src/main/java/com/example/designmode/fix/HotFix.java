package com.example.designmode.fix;

import android.app.Application;
import android.os.Build;

import com.example.designmode.utils.ShareReflectUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HotFix {

    private static String TAG = "HotFix";

    /**
     * 1.获取程序的pathclassloader对象
     * 2.反射获取pathclassloader的父类BaseDexClassLoader的pathList对象
     * 3.反射获取pathList的dexElements对象
     * 4.把补丁包变成element数组：pathElement(反射执行makeElement方法)
     * 5.合并pathElement和oldElement
     * 6.反射把oldElement赋值成newElement
     * @param application
     * @param patch
     */
    public static void installPath(Application application, File patch) {

        if (patch == null) return;
        ClassLoader pathClassLoader = application.getClassLoader();
        try {
            Field pathListField = ShareReflectUtil.findField(pathClassLoader, "pathList");
            Object pathList = pathListField.get(pathClassLoader);
            Field dexElementsField = ShareReflectUtil.findField(pathList, "dexElements");
            Object[] oldElements = (Object[]) dexElementsField.get(pathList);
            Method makePathElement = null;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                makePathElement = ShareReflectUtil.findMethod(pathList, "makePathElement",
                        List.class, File.class, List.class);
            }
            List<File> patchs = new ArrayList<>();
            patchs.add(patch);
            ArrayList<IOException> ioExceptions = new ArrayList<>();


            Object[] patchElements = (Object[]) makePathElement.invoke(pathList, patchs, application.getCacheDir(), ioExceptions);
            Object[] newElements = (Object[]) Array.newInstance(oldElements.getClass().getComponentType(),
                    oldElements.length + patchElements.length);
            System.arraycopy(patchElements, 0, newElements, 0, patchElements.length);
            System.arraycopy(oldElements, 0, newElements, patchElements.length, oldElements.length);
            dexElementsField.set(pathList, newElements);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
