package com.example.designmode.performance.traffic;

import android.app.Activity;
import android.net.TrafficStats;

import com.example.designmode.performance.BaseTracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrafficCheck extends BaseTracker {

    public static volatile TrafficCheck instance;
    private List<TrafficListener> trafficListeners = new ArrayList<>();
    private Map<Activity, Traffic> hashMap = new HashMap<>();
    private static int sequence;
    private long currentStats;

    private TrafficCheck() {}

    public static TrafficCheck getInstance() {
        if (instance == null) {
            synchronized (TrafficCheck.class) {
                if (instance == null) instance = new TrafficCheck();
            }
        }
        return instance;
    }

    private interface TrafficListener { void getTrafficStats(Activity activity, long value);}
    private void addTrafficListener(TrafficListener listener) {trafficListeners.add(listener);}
    private void removeTrafficListneer(TrafficListener listener) {trafficListeners.remove(listener);}

    public void markAcitvityStart(Activity activity) {
        if (hashMap.get(activity) == null) {
            Traffic item = new Traffic();
            item.activity = activity;
            item.trafficCost = 0;
            item.sequence = sequence++;
            item.activityName = activity.getClass().getSimpleName();
            hashMap.put(activity, item);
        }
        //核心方法
        currentStats = TrafficStats.getUidRxBytes(android.os.Process.myUid());
    }

    public void markActivityPause(Activity activity) {
        Traffic item = hashMap.get(activity);
        if (item != null)
            item.trafficCost = TrafficStats.getUidRxBytes(android.os.Process.myUid()) - currentStats;
    }

    public void markActivityDestory(Activity activity) {
        Traffic item = hashMap.get(activity);
        if (item != null) {
            for (TrafficListener listener : trafficListeners) {
                listener.getTrafficStats(item.activity, item.trafficCost);
                hashMap.remove(activity);
            }
            item.activity = null;
        }
    }

}
