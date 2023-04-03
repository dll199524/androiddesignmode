package com.example.designmode;

import android.app.Application;
import android.content.Context;
import android.os.Debug;
import android.os.Message;
import android.os.MessageQueue;
import android.os.StrictMode;

import com.example.designmode.performance.start.StartUpManager;
import com.example.designmode.performance.start.task.Task1;
import com.example.designmode.performance.start.task.Task2;
import com.example.designmode.performance.start.task.Task3;
import com.example.designmode.performance.start.task.Task4;
import com.example.designmode.performance.start.task.Task5;

public class MyApplication extends Application {
    private MessageQueue.IdleHandler mIdleHandler;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {

        Debug.startMethodTracing("test");
        if (BuildConfig.DEBUG) {
            //线程检测策略
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork() //or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build()
            );

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .detectActivityLeaks()
                    .penaltyLog()
                    .penaltyDeath()
                    .build()
            );
        }
        super.onCreate();

        //延迟加载在主线程空闲的时候
        mIdleHandler = new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                return false;
            }
        };

        new Task1().create(this);
        new Task2().create(this);
        new Task3().create(this);
        new Task4().create(this);
        new Task5().create(this);

        new StartUpManager.Bulider()
                .addStartUp(new Task5())
                .addStartUp(new Task4())
                .addStartUp(new Task3())
                .addStartUp(new Task2())
                .addStartUp(new Task1())
                .bulider(this).start();

    }
}
