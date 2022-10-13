package com.example.designmode.performance;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityStack {

    private static volatile ActivityStack instance;
    private volatile int currentState;
    List<Activity> activitys = new ArrayList<>();


    private ActivityStack() {}
    public static ActivityStack getInstance() {
        if (instance == null) {
            synchronized (ActivityStack.class) {
                if (instance == null)
                    instance = new ActivityStack();
            }
        }
        return instance;
    }

    public void push(Activity activity) {activitys.add(0, activity);}
    public void pop(Activity activity) {activitys.remove(activity);}
    public int getSize() {return activitys.size();}
    public Activity getTop() {return activitys.size() > 0 ? activitys.get(0) : null;}
    public Activity getBottom() {return activitys.size() > 0 ? activitys.get(activitys.size() - 1) : null;}
    public void markStart() {currentState++;}
    public void markEnd() {currentState--;}

    public boolean isInBackGround() {return currentState == 0;}


}
