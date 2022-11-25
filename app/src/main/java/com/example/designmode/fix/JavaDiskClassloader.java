package com.example.designmode.fix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class JavaDiskClassloader extends ClassLoader{


    private String path;
    public JavaDiskClassloader(String path) {
        this.path = path;
    }
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class clazz = null;
        byte[] classDate = loadClassData(name);
        if (classDate == null)
            throw new ClassNotFoundException();
        else
            clazz = defineClass(name, classDate, 0, classDate.length);
        return clazz;
    }

    private byte[] loadClassData(String name){
        String fileName = getFileName(name);
        File file = new File(path, name);
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            is = new FileInputStream(file);
            baos = new ByteArrayOutputStream();
            byte[] butter = new byte[1024];
            int len = 0;
            while ((len = is.read(butter)) != -1)
                baos.write(butter, 0, butter.length);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (baos != null) baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String getFileName(String name) {
        int index = name.lastIndexOf(".");
        if (index == -1)
            return name + ".class";
        else return name.substring(index + 1) + ".class";
    }
}
