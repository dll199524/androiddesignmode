package com.example.designmode.performance.battery;


import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

//电量监控



public class BatteryStatusTracker extends BaseTracker {

    private static volatile BatteryStatusTracker instance;
    private HandlerThread handlerThread;
    private Handler mHandler;
    private String display;
    private int startPercent;
    private List<BatteryListener> batteryListeners = new ArrayList<>();

    private BatteryStatusTracker() {
        handlerThread = new HandlerThread("BatteryStatus", Thread.NORM_PRIORITY);
        mHandler = new Handler(handlerThread.getLooper());
    }

    public static BatteryStatusTracker getInstance() {
        if (instance == null) {
            synchronized (BatteryStatusTracker.class) {
                if (instance == null)
                    instance = new BatteryStatusTracker();
            }
        }
        return instance;
    }

    @Override
    public void startTrack(Application application) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                Intent batteryStatus = activity.getApplication().registerReceiver(null, intentFilter);
                startPercent = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            }
        });

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (batteryListeners.size() > 0) {
                    BatteryInfo batteryInfo = getBatteryInfo(activity.getApplication());
                    for (BatteryListener listener : batteryListeners)
                        listener.onBatteryCost(batteryInfo);
                }
            }
        });
    }


    private BatteryInfo getBatteryInfo(Application application) {
        if (TextUtils.isEmpty(display)) {
            display = "" + application.getResources().getDisplayMetrics().widthPixels + " * " +
                    application.getResources().getDisplayMetrics().heightPixels;
        }
        BatteryInfo batteryInfo = new BatteryInfo();
        try {
            IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = application.registerReceiver(null, filter);
            int status = batteryStatus.getIntExtra("status", 0);
            boolean ischarging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            batteryInfo.charging = ischarging;
            batteryInfo.cost = ischarging ? 0 : startPercent - batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            batteryInfo.duration += (SystemClock.uptimeMillis() - LauncherHelperProvider.mStartUpTimeStamp);
            batteryInfo.total = scale;
            batteryInfo.display = display;
            batteryInfo.screenBrightness = getSystemScreenBrightness(application);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return batteryInfo;
    }

    private int getSystemScreenBrightness(Application application) {
        ContentResolver contentResolver = application.getContentResolver();
        int defaultVal = 125;
        return Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, defaultVal);
    }


    public interface BatteryListener {
        void onBatteryCost(BatteryInfo batteryInfo);
    }

    public void addBatteryListener(BatteryListener batteryListener) {batteryListeners.add(batteryListener);}
    public void removeBatteryListener(BatteryListener batteryListener) {batteryListeners.remove(batteryListener);}
}
