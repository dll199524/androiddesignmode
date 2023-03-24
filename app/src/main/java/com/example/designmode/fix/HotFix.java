package com.example.designmode.fix;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
        //防止类被打上CLASS_ISPREVERIFIED标志把补丁包的类也加入到dexElement里面去
        File hackFile = initHack(application);
        if (patch == null) return;
        ClassLoader pathClassLoader = application.getClassLoader();
        List<File> patchs = new ArrayList<>();
        if (patch.exists())
            patchs.add(patch);
        patchs.add(hackFile);

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

    public static void installPathAll(Application application, File patch) throws Throwable {
        File hackFile = initHack(application);
        List<File> patchs = new ArrayList<>();
        if (patch.exists())
            patchs.add(patch);
        patchs.add(hackFile);
        File dexOptDir = application.getCacheDir();
        ClassLoader classLoader = application.getClassLoader();
        //android 7.0 APT预编译用我们自己的classLoader替换系统的 高版本classloader存在缓存
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            InjectClassLoader.inject(application, classLoader, patchs);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                V23.install(classLoader, patchs, dexOptDir);
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                V19.install(classLoader, patchs, dexOptDir);
            else V14.install(classLoader, patchs, dexOptDir);
        }

    }

    private static class V23 {
        public static void install(ClassLoader classLoader, List<File> patchs, File dexOptDir) throws Exception{
            //找到pathList
            Field pathListField = ShareReflectUtil.findField(classLoader, "pathLists");
            Object dexPathList = pathListField.get(classLoader);
            
            ArrayList<IOException> suppressExpetions = new ArrayList<>();
            Object[] newElements = makeElements(dexPathList, new ArrayList<>(patchs), dexOptDir, suppressExpetions);
            ShareReflectUtil.combineElements(classLoader, "dexElements", newElements);
            
        }

        private static Object[] makeElements(Object dexPathList, ArrayList<File> files, File dexOptDir, ArrayList<IOException> suppressExpetions) throws Exception{
            Method makeElements = ShareReflectUtil.findMethod(dexPathList, "makePathElements",
                    List.class, File.class, File.class);
            return (Object[]) makeElements.invoke(dexPathList, files, dexOptDir, suppressExpetions);
        }



    }

    private static class V19 {
        public static void install(ClassLoader classLoader, List<File> patchs, File dexOptDir) throws Exception{
            Field pathListField = ShareReflectUtil.findField(classLoader, "pathLists");
            Object dexPathList = pathListField.get(classLoader);
            ArrayList<IOException> suppressedExceptions = new ArrayList<>();
            ShareReflectUtil.combineElements(classLoader, "dexElements",
                    makeElements(dexPathList, new ArrayList<>(patchs), dexOptDir, suppressedExceptions));
        }
        private static Object[] makeElements(Object dexPathList, ArrayList<File> files, File dexOptDir, ArrayList<IOException> suppressExpetions) throws Exception{
            Method makeElements = ShareReflectUtil.findMethod(dexPathList, "makePathElements",
                    ArrayList.class, File.class, ArrayList.class);
            return (Object[]) makeElements.invoke(dexPathList, files, dexOptDir, suppressExpetions);
        }
    }

    private static class V14 {
        public static void install(ClassLoader classLoader, List<File> patchs, File dexOptDir) throws Exception{
            Field pathListField = ShareReflectUtil.findField(classLoader, "pathLists");
            Object dexPathList = pathListField.get(classLoader);

            ShareReflectUtil.combineElements(classLoader, "dexElements",
                    makeElements(dexPathList, new ArrayList<>(patchs), dexOptDir));
        }
        private static Object[] makeElements(Object dexPathList, ArrayList<File> files, File dexOptDir) throws Exception{
            Method makeElements = ShareReflectUtil.findMethod(dexPathList, "makePathElements",
                    ArrayList.class, File.class);
            return (Object[]) makeElements.invoke(dexPathList, files, dexOptDir);
        }
    }

    //防止类被打上CLASS_ISPREVERIFIED标志
    private static File initHack(Context context) {
        File hackDir = context.getDir("hack", Context.MODE_PRIVATE);
        File hackFile = new File(hackDir, "hack.jar");
        if (!hackFile.exists()) {
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                bis = new BufferedInputStream(context.getAssets().open("hack" + ".jar"));
                bos = new BufferedOutputStream(new FileOutputStream(hackFile));
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = (bis.read(buffer))) != -1) {
                    bos.write(buffer);
                }
            } catch (IOException e) {e.printStackTrace();}
            finally {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return hackFile;
    }
}
