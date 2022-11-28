package com.example.designmode.fix;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;

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
        Field nativeLibraryDirectoriesField = ShareReflectUtil.findField(oldPathList, "nativeLibraryDirectories");
        List<File> nativeLibraryDirectories = (List<File>) nativeLibraryDirectoriesField.get(oldPathList);
        StringBuilder libraryPathBuilder = new StringBuilder();
        isFirstItem = true;
        for (File nativeLibraryDirectory : nativeLibraryDirectories) {
            if (nativeLibraryDirectory == null) continue;
            if (isFirstItem) isFirstItem = false;
            else libraryPathBuilder.append(File.pathSeparator);
            libraryPathBuilder.append(nativeLibraryDirectory.getAbsolutePath());
        }

        String librarySearchPath = libraryPathBuilder.toString();
        return new MyClassLoader(dexPath, librarySearchPath, ClassLoader.getSystemClassLoader());
    }


    private static void doInject(Application application, ClassLoader newClassLoader) throws NoSuchFieldException, IllegalAccessException {
        Thread.currentThread().setContextClassLoader(newClassLoader);
        Context baseContext = (Context) ShareReflectUtil.findField(application, "mBase").get(application);
        if (Build.VERSION.SDK_INT  >= 26)
            ShareReflectUtil.findField(baseContext, "mClassLoader").set(baseContext, newClassLoader);
        if (Build.VERSION.SDK_INT < 27) {
            Resources resources = application.getResources();
            try {
                ShareReflectUtil.findField(resources, "mClassLoader").set(resources, newClassLoader);
                final Object drawableInflater = ShareReflectUtil.findField(resources, "mDrawableInflater");
                if (drawableInflater != null) ShareReflectUtil.findField(drawableInflater, "mClassLoader").set(drawableInflater, newClassLoader);
            } catch (Throwable e) {

            }
        }
    }
}
