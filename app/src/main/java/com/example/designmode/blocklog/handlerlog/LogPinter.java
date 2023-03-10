package com.example.designmode.blocklog.handlerlog;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.Printer;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;


//app 卡顿 线上卡顿
public class LogPinter implements Printer {

    public static final String ACTION_BLOCK = "com.example.designmode";
    public static final String EXTRA_START_TIME = "block_start_time";
    public static final String EXTRA_FINISH_TIME = "block_finish_time";

    private final String TAG = "LogPrinter";
    private Context mContext;
    private long startTimeMills, endTimeMills;
    private StackInfoCatcher mStackInfoCatcher;


    public LogPinter(Context context) {
        this.mContext = context;
        mStackInfoCatcher = new StackInfoCatcher(mContext);
        mStackInfoCatcher.start();
    }

    @Override
    public void println(String x) {
        switch (isStart(x)) {
            case 1:
                startTimeMills = System.currentTimeMillis();
                break;
            case 0:
                endTimeMills = System.currentTimeMillis();
                if (isBlock(endTimeMills, startTimeMills))
                    notifyBlockEvent(endTimeMills, startTimeMills);
                break;
            default:
                break;
        }
    }

    private void notifyBlockEvent(long endTimeMills, long startTimeMills) {
        Log.d(TAG, "block-time: " + (endTimeMills - startTimeMills));
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        Intent in = new Intent(ACTION_BLOCK);
        in.putExtra(EXTRA_START_TIME, startTimeMills);
        in.putExtra(EXTRA_FINISH_TIME, endTimeMills);
        localBroadcastManager.sendBroadcast(in);
    }

    private boolean isBlock(long endTimeMills, long startTimeMills) {
        return endTimeMills - startTimeMills > 500;
    }

    private int isStart(String x) {
        if (!TextUtils.isEmpty(x)) {
            if (x.startsWith(">>>>> Dispatching to "))
                return 1;
            else if (x.startsWith("<<<<< Finished to "))
                return 0;
        }
        return -1;
    }
}
