package com.example.designmode.mod;

import android.app.Activity;

import java.util.Iterator;
import java.util.Stack;

public class ActivityManager {
    public static volatile ActivityManager instance;
    private Stack<Activity> activities = new Stack<>();
    public static ActivityManager getInstance() {
        if (instance == null) {
            synchronized (ActivityManager.class) {
                if (instance == null) instance = new ActivityManager();
            }
        }
        return instance;
    }

    public void attach(Activity activity) {
        activities.add(activity);
    }

    public void dettch(Activity dettchActivity) {
        int size = activities.size();
        for (int i = 0; i < size; i++) {
            Activity activity = activities.get(i);
            if (activity == dettchActivity) {
                activities.remove(activity);
                i--;
                size--;
            }
        }
    }

    public void finish(Activity finishActivity) {
        int size = activities.size();
        for (int i = 0; i < size; i++) {
            Activity activity = activities.get(i);
            if (activity == finishActivity) {
                activities.remove(activity);
                activity.finish();
                i--;
                size--;
            }
        }
    }

    public void finish(Class<? extends Activity> activityclass) {
        int size = activities.size();
        for (int i = 0; i < size; i++) {
            Activity activity = activities.get(i);
            if (activity.getClass().getCanonicalName().equals(activityclass.getCanonicalName())) {
                activities.remove(i);
                activity.finish();
                i--;
                size--;
            }
        }
    }

    public void finishAll() {
        Iterator<Activity> activityIterable = activities.iterator();
        while (activityIterable.hasNext()) {
            Activity activity = activityIterable.next();
            activities.remove(activity);
            activity.finish();
        }
    }

    public Activity currentActivity() {return activities.lastElement();}

    public void exitApplication() {
        finishAll();
        System.exit(0);
    }


}
