package com.example.designmode;

import android.app.Application;
import android.content.Context;
import android.os.Debug;
import android.os.StrictMode;

public class MyApplication extends Application {

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
    }
}
