package com.example.designmode.fix;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.BaseDexClassLoader;


public class FixManager {
    private Context mContext;
    private final File mDexFile;
    private static final String TAG = "FixManager";

    public FixManager(Context context) {
        this.mContext = context;
        //获取应用可以访问的dex目录
        mDexFile = context.getDir("odex", Context.MODE_PRIVATE);
    }
    
    public void fixBugByDex() throws Exception {
        //1.获取Android系统中原来的dex list
        Object originDexElements = loadOriginDex();
        Log.d(TAG, "fixBugByDex: loadOriginDex end");
        //2.准备dex解压路径 解压路径是拷贝路径的一个子文件夹
        File unZipDir = prepareUnZipDir();
        Log.d(TAG, "fixBugByDex: prepare unzipdir end" + unZipDir);
        //3.查看是否有dex文件 如果存在 copy到指定目录,只有在app内部的目录才能解压和解析dex文件
        File srcFile = findDexFiles();
        File destFile = new File(mDexFile, srcFile.getName());
        copyFile(srcFile, destFile);
        //4.获取copy目录中的所有dex补丁包(*.dex)
        List<File> allFixDexFile = loadAllDexFile();
        if (allFixDexFile == null || allFixDexFile.size() == 0) {
            Log.d(TAG, "fixBugByDex: dex size is incorrect");
            return;
        } else {
            Log.d(TAG, "fixBugByDex: dex size is " + allFixDexFile.size());
        }
        //5.合并所有补丁dex包以及运行中的dex包
        originDexElements = combineDex(allFixDexFile, unZipDir, originDexElements);
        Log.d(TAG, "fixBugByDex: combine end");
        //6.合并所有补丁dex包以及运行中的dex包
        InjectDexToClassLoader(originDexElements);
    }


    private Object loadOriginDex() throws Exception {
        ClassLoader applicationClassLoader = mContext.getClassLoader();
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(applicationClassLoader);
        if (pathList == null)
            return null;
        Field dexElementField = pathList.getClass().getField("dexElements");
        dexElementField.setAccessible(true);
        return dexElementField.get(pathList);
    }

    private File prepareUnZipDir() {
        File unZipDir = new File(mDexFile, "odex");
        if (!unZipDir.exists()) {
            boolean res = unZipDir.mkdirs();
            if (!res) Log.d(TAG, "prepareUnZipDir: mkdir fail");
        }
        return unZipDir;
    }

    private File findDexFiles() {
        File fixFile = new File(mContext.getExternalFilesDir(null), "fix2.dex");
        if (fixFile.exists()) {
            Log.d(TAG, "findDexFiles: " + fixFile.getAbsolutePath());
        } else {
            boolean b = fixFile.mkdirs();
            if (b) Log.d(TAG, "create success" + fixFile.getAbsolutePath());
            else Log.d(TAG, "create falied" + fixFile.getAbsolutePath());
        }
        return fixFile;
    }


    private void copyFile(File srcFile, File destFile) throws IOException {
        FileChannel fis = null;
        FileChannel fos = null;
        try {
            if (!destFile.exists()) destFile.createNewFile();
            fis = new FileInputStream(srcFile).getChannel();
            fos = new FileOutputStream(destFile).getChannel();
            fis.transferTo(0, fis.size(), fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private List<File> loadAllDexFile() {
        File[] dexFiles = mDexFile.listFiles();
        if (dexFiles == null || dexFiles.length == 0) {
            Log.d(TAG, "dexfile is null or dexfile size is 0");
            return null;
        }
        List<File> allDexFiles = new ArrayList<>();
        for (File dexFile : dexFiles) {
            if (dexFile.getName().endsWith(".dex"))
                allDexFiles.add(dexFile);
        }
        return allDexFiles;
    }

    private Object combineDex(List<File> allFixDexFile, File unZipDir, Object originDexElements) throws Exception {
        ClassLoader applicationClassLoader = mContext.getClassLoader();
        for (File fixFile : allFixDexFile) {
            ClassLoader fixClassLoader = new BaseDexClassLoader(
                    fixFile.getAbsolutePath(),
                    unZipDir,
                    null,
                    applicationClassLoader);
            Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
            pathListField.setAccessible(true);
            Object pathList = pathListField.get(fixClassLoader);
            Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
            dexElementsField.setAccessible(true);
            Object fixDexElements = dexElementsField.get(pathList);
            Class<?> loadClass = fixDexElements.getClass().getComponentType();
            int i = Array.getLength(fixDexElements);
            int j = Array.getLength(originDexElements) + i;
            Object result = Array.newInstance(loadClass, j);
            for (int k = 0; k < j; k++) {
                if (k < i)
                    Array.set(result, k, Array.get(fixDexElements, k));
                else Array.set(result, k, Array.get(originDexElements, k));
            }
            originDexElements = result;
        }
        return originDexElements;
    }

    private void InjectDexToClassLoader(Object originDexElements) throws Exception{
        ClassLoader applicationClassLoader = mContext.getClassLoader();
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(applicationClassLoader);

        Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        dexElementsField.set(pathList, originDexElements);
    }

}
