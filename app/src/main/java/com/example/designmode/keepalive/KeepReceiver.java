package com.example.designmode.keepalive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public class KeepReceiver extends BroadcastReceiver {

    private static final String TAG = "KeepReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.equals(action, Intent.ACTION_SCREEN_OFF))
            KeepManager.getInstance().startKeep(context);
        else if (TextUtils.equals(action, Intent.ACTION_SCREEN_ON))
            KeepManager.getInstance().finishKeep();
    }
}
