package com.example.designmode.keepalive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.lang.ref.WeakReference;

public class KeepManager {

    public static volatile KeepManager instance;
    private WeakReference<Activity> mKeepActivity;
    private KeepReceiver keepReceiver;

    public static KeepManager getInstance() {
        if (instance == null) {
            synchronized (KeepManager.class) {
                if (instance == null) instance = new KeepManager();
            }
        }
        return instance;
    }

    public void startKeep(Context context) {
        Intent intent = new Intent(context, KeepActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void finishKeep() {
        if (mKeepActivity != null) {
            Activity activity = mKeepActivity.get();
            if (activity != null) activity.finish();
        }
        mKeepActivity = null;
    }

    public void registerKeepReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        keepReceiver = new KeepReceiver();
        context.registerReceiver(keepReceiver, filter);
    }

    public void unRegisterKeepReceiver(Context context) {
        if (keepReceiver != null) context.unregisterReceiver(keepReceiver);
    }


    public void setKeepActivity(KeepActivity keepActivity) {
        mKeepActivity = new WeakReference<>(keepActivity);
    }

}
