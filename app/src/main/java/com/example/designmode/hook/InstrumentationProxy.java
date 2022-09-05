package com.example.designmode.hook;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.Method;

public class InstrumentationProxy extends Instrumentation {
    private static String TAG = "InstrumentationProxy";
    Instrumentation mInstrumentation;
    public InstrumentationProxy(Instrumentation instrumentation) {this.mInstrumentation = instrumentation;}
    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {
        Log.d(TAG, "hook成功 who: " + who);
        try {
            Method execStartActivity = Instrumentation.class.getMethod("execStartActivity",
                    Context.class, IBinder.class, IBinder.class, Activity.class, Intent.class, int.class, Bundle.class);
            execStartActivity.setAccessible(true);
            return (ActivityResult) execStartActivity.invoke(mInstrumentation, who, contextThread, token, target, intent, requestCode, options);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
