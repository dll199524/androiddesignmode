package com.example.designmode.performance.start.task;

import android.content.Context;
import android.os.Looper;
import android.os.SystemClock;

import com.example.designmode.performance.start.AndroidStartUp;
import com.example.designmode.performance.start.StartUp;


import java.util.List;

public class Task1 extends AndroidStartUp<String> {

    @Override
    public List<Class<? extends StartUp<?>>> dependencies() {
        return null;
    }

    @Override
    public boolean isMainThread() {
        return true;
    }

    @Override
    public boolean waitMainThread() {
        return false;
    }

    @Override
    public String create(Context context) {
        String t = Looper.myLooper() ==
                Looper.getMainLooper() ? "主线程" : "子线程";
        LogUtils.log(t + " task1 runing...");
        SystemClock.sleep(3000);
        return "task1 end...";
    }
}
