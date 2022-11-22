package com.example.arouter_compiler;

public class MyRouter {

    private volatile static MyRouter instance;
    private MyRouter() {}

    public static MyRouter getInstance() {
        if (instance == null) {
            synchronized (MyRouter.class) {
                if (instance == null) instance = new MyRouter();
            }
        }
        return instance;
    }


}
