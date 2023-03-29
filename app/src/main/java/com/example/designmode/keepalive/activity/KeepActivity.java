package com.example.designmode.keepalive.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

//1.activity提权
public class KeepActivity extends Activity {

    private static final String TAG = KeepActivity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setGravity(Gravity.START | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = 0;
        params.height = 0;
        window.setAttributes(params);

        KeepManager.getInstance().setKeepActivity(this);
    }
}
