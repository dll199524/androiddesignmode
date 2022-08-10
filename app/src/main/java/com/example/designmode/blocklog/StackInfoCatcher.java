package com.example.designmode.blocklog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Looper;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

public class StackInfoCatcher extends Thread {

    private List<StackTraceInfo> list = new ArrayList<>(1024);
    private Context mContext;
    private BroadcastReceiver broadcastReceiver;
    private String TAG = "StackInfoCather";
    private long lastTime = 0;
    private boolean stop = false;

    public StackInfoCatcher(Context context) {
        this.mContext = context;
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long startTime = intent.getLongExtra(LogPinter.EXTRA_START_TIME, 0);
                long endTime = intent.getLongExtra(LogPinter.EXTRA_FINISH_TIME, 0);
                StackTraceInfo info = getStackInfo(startTime, endTime);
                if (info != null) {
                    Log.d(TAG, "find block line " + info);
                    Log.d(TAG, info.mLog);
                }
                else
                    Log.d(TAG, "no block line find");
            }
        };
        manager.registerReceiver(broadcastReceiver, new IntentFilter(LogPinter.ACTION_BLOCK));
    }

    private StackTraceInfo getStackInfo(long startTime, long endTime) {
        for (StackTraceInfo info : list) {
            if (info.mTime >= startTime && info.mTime < endTime)
                return info;
        }
        return null;
    }

    @Override
    public void run() {
        super.run();
        while (!stop) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTime > 500) {
                lastTime = currentTime;
                StackTraceInfo info = new StackTraceInfo();
                info.mTime = currentTime;
                info.mLog = getStackInfoToString(Looper.getMainLooper().getThread().getStackTrace());
                list.add(info);
            }
            if (list.size() > 1024) list.remove(0);
        }
    }

    private String getStackInfoToString(StackTraceElement[] stackTrace) {
        StringBuilder sb = new StringBuilder();
        if (stackTrace != null && stackTrace.length > 0) {
            for (int i = 0; i < stackTrace.length; i++) {
                sb.append("\tat ");
                sb.append(stackTrace[i].toString());
                sb.append("\n");
            }
        }
        return sb.toString();
    }


    public void stopTrace() {
        stop = true;
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(broadcastReceiver);
    }
}
