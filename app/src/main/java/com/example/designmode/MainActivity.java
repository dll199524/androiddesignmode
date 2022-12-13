package com.example.designmode;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.StatusBarManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.designmode.hook.InstrumentationProxy;
import com.example.designmode.performance.UserManager;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserManager userManager = UserManager.getInstance(this);
        replaceActivityIntrumentation(this);
        Intent in = new Intent(Intent.ACTION_VIEW);
        in.setData(Uri.parse("http://liuwangshu.cn/"));
        startActivity(in);

    }



    //hook activity startActivity方法
    public void replaceActivityIntrumentation(Activity activity) {
        try {
            Field field = Activity.class.getDeclaredField("mIntrumentation");
            field.setAccessible(true);
            Instrumentation instrumentation = (Instrumentation) field.get(activity);
            InstrumentationProxy proxy = new InstrumentationProxy(instrumentation);
            field.set(activity, proxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //hook context startAcitivity方法
    public void replaceActvityIntrumentation() {
        try {
            Class<?> activityThreadClazz = Class.forName("android.app.ActivityThread");
            Field field = activityThreadClazz.getDeclaredField("mCurrentActivityThreaad");
            field.setAccessible(true);
            Object currentActivityThread = field.get(null);
            Field intrumentationField = currentActivityThread.getClass().getField("mIntrumentation");
            intrumentationField.setAccessible(true);
            Instrumentation instrumentation = (Instrumentation) intrumentationField.get(currentActivityThread);
            Instrumentation proxy = new InstrumentationProxy(instrumentation);
            intrumentationField.set(activityThreadClazz, proxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}