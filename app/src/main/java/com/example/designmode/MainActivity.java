package com.example.designmode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.designmode.hook.InstrumentationProxy;
import com.example.designmode.utils.BitmapUtils;

import java.lang.reflect.Field;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Log.d(TAG, "onCreate: " + this.hashCode());
//        UserManager userManager = UserManager.getInstance(this);
//        replaceActivityIntrumentation(this);
//        Intent in = new Intent(Intent.ACTION_VIEW);
//        in.setData(Uri.parse("http://liuwangshu.cn/"));
//        startActivity(in);
//        RequestManager requestManager = Glide.with(this);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_1);
        bitmap = BitmapUtils.getBitmap(bitmap);
//        bitmap = BitmapUtils.grayPixels(bitmap);
        iv = findViewById(R.id.iv);
        iv.setImageBitmap(bitmap);

        Intent intent = new Intent(MainActivity.this, ProxyActivity.class);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Debug.stopMethodTracing();
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
            Field field = activityThreadClazz.getDeclaredField("mCurrentActivityThread");
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

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ..................");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ..................");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ..................");
//        Log.d(TAG, "onCreate: " + iv.getMeasuredHeight());

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ..................");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ..................");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ..................");

    }
}