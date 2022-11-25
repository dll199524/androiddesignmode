package com.example.designmode.fix;

import android.app.Application;
import android.content.Context;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

import dalvik.system.DexFile;

public class InjectClassLoader extends ClassLoader{

    public static void inject(Application application, ClassLoader oldClassLoader, List<File> patchs) throws Throwable {
        ClassLoader newClassLoader = createClassLoader(application, oldClassLoader, patchs);
        doInject(application, newClassLoader);
    }



    private static ClassLoader createClassLoader(Context context, ClassLoader oldClassLoader, List<File> patchs) throws Throwable{
        //拼补丁包的dex
        StringBuilder dexPathBulider = new StringBuilder();
        boolean isFirstItem = true;
        for (File file : patchs) {
            if (isFirstItem) isFirstItem = false;
            else dexPathBulider.append(File.pathSeparator);
            dexPathBulider.append(file.getAbsolutePath());
        }

        //把apk的补丁包拼起来
        Field pathListField = ShareReflectUtil.findField(oldClassLoader, "pathList");
        Object oldPathList = pathListField.get(oldClassLoader);

        Field dexElementsField = ShareReflectUtil.findField(oldPathList, "dexElements");
        Object[] oldDexElements = (Object[]) dexElementsField.get(oldClassLoader);

        Field dexFileField = ShareReflectUtil.findField(oldDexElements[0], "dexFile");
        String packageName = context.getPackageName();

        isFirstItem = true;
        for (Object oldDexElement : oldDexElements) {
            String dexPath = null;
            DexFile dexFile = (DexFile) dexFileField.get(oldDexElement);
            if (dexFile != null) dexPath = dexFile.getName();
            if (dexPath == null || dexPath.isEmpty()) continue;
            if (!dexPath.contains("/" + packageName)) continue;
            if (isFirstItem) isFirstItem = false;
            else dexPathBulider.append(File.pathSeparator);
            dexPathBulider.append(dexPath);
        }
        String dexPath = dexPathBulider.toString();

        //apk的so加载路径


        String librarySearchPath = null;



        return new MyClassLoader(dexPath, librarySearchPath, ClassLoader.getSystemClassLoader());
    }


    private static void doInject(Application application, ClassLoader newClassLoader) {

    }
}
