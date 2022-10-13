package com.example.designmode.performance.mem;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Debug;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.designmode.performance.ActivityStack;
import com.example.designmode.performance.BaseTracker;
import com.example.designmode.utils.ProcessUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class MemoryTracker extends BaseTracker {

    private static volatile MemoryTracker instance;
    private HandlerThread handlerThread = new HandlerThread("BatteryStatus", Thread.NORM_PRIORITY);
    private Handler handler = new Handler(handlerThread.getLooper());
    private WeakHashMap<Activity, String> weakHashMap = new WeakHashMap<>();
    private Set<TrackMemoryListener> memoryListeners = new HashSet<>();
    private String display;


    public static MemoryTracker getInstance() {
        if (instance == null) {
            synchronized (MemoryTracker.class) {
                if (instance == null)
                    instance = new MemoryTracker();
            }
        }
        return instance;
    }


    @Override
    public void startTrack(Application application) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (memoryListeners.size() > 0 && !ActivityStack.getInstance().isInBackGround()) {
                    MemoryInfo memoryInfo = collectMemoryInfo(application);
                    for (TrackMemoryListener memoryListener : memoryListeners)
                        memoryListener.onCurrentMemoryCost(memoryInfo);
                }
                handler.postDelayed(this, 30 * 1000);
            }

        }, 30 * 1000);
    }


    @Override
    public void destroy(Application application) {
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        if (!ActivityStack.getInstance().isInBackGround()) return;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mallocBigMem();
                Runtime.getRuntime().gc();
            }
        }, 1000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!ActivityStack.getInstance().isInBackGround()) return;
                    mallocBigMem();
                    Runtime.getRuntime().gc();
                    SystemClock.sleep(100);
                    System.runFinalization();
                    HashMap<String, Integer> hashMap = new HashMap<>();
                    for (Map.Entry<Activity, String> activityStringEntry : weakHashMap.entrySet()) {
                        String name = activityStringEntry.getKey().getClass().getSimpleName();
                        Integer value = hashMap.get(name);
                        if (value == null)
                            hashMap.put(name, value);
                        else hashMap.put(name, value + 1);
                    }
                    if (memoryListeners.size() > 0) {
                        for(Map.Entry<String, Integer> entry : hashMap.entrySet()) {
                            for (TrackMemoryListener listener : memoryListeners)
                                listener.onLeakActivity(entry.getKey(), entry.getValue());
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1000);
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        weakHashMap.put(activity, activity.getClass().getSimpleName());
    }



    private MemoryInfo collectMemoryInfo(Application application) {
        MemoryInfo traceMemoryInfo = new MemoryInfo();
        MemoryInfo.AppMemory appMemory = new MemoryInfo.AppMemory();
        MemoryInfo.SystemMemory systemMemory = new MemoryInfo.SystemMemory();
        if (TextUtils.isEmpty(display))
            display = "" + application.getResources().getDisplayMetrics().widthPixels + " * " +
                    application.getResources().getDisplayMetrics().heightPixels;

        //系统内存
        ActivityManager activityManager = (ActivityManager) application.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        systemMemory.availMem = memoryInfo.availMem >> 20;
        systemMemory.totalMem = memoryInfo.totalMem >> 20;
        systemMemory.lowMem = memoryInfo.lowMemory;
        systemMemory.threshold = memoryInfo.threshold >> 20;

        //进程native内存
        Debug.MemoryInfo debugMemoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(debugMemoryInfo);
        appMemory.nativePss = debugMemoryInfo.nativePss >> 10;
        appMemory.dalvikPss = debugMemoryInfo.dalvikPss >> 10;
        appMemory.totalPss = debugMemoryInfo.getTotalPss() >> 10;
        appMemory.memoryInfo = debugMemoryInfo;

        traceMemoryInfo.appMemory = appMemory;
        traceMemoryInfo.systemMemory = systemMemory;
        traceMemoryInfo.display = display;
        traceMemoryInfo.procName = ProcessUtil.getCurrentProcessName(application);
        traceMemoryInfo.activityCount = ActivityStack.getInstance().getSize();

        return traceMemoryInfo;
    }

    public interface TrackMemoryListener {
        void onLeakActivity(String Activity, int count);
        void onCurrentMemoryCost(MemoryInfo memoryInfo);
    }

    public void addMemoryLeakListener(TrackMemoryListener leakListener) {memoryListeners.add(leakListener);}

    public void removeMemoryLeakLintener(TrackMemoryListener leakListener) {memoryListeners.remove(leakListener);}

    public void mallocBigMem() {
        byte[] bigBytes = new byte[4 * 1024 * 1024];
        for (int i = 0; i < bigBytes.length; i += 1024)
            bigBytes[i] = 1;
    }
}
