package com.example.designmode.eventBus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.designmode.R;

public class EventBusTestActivity extends AppCompatActivity {

    private static final String TAG = EventBusTestActivity.class.getSimpleName();
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus_test);
        EventBus.getDefault().register(this);
        //进入测试页面
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EventBusTestActivity.this, EventBusTestActivity1.class);
                startActivity(in);
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 50, stiky = false)
    public void test1(String msg) {
        Log.d(TAG, "test1: " + msg);
        textView.setText(msg);
    }
    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void test2(String msg) {
        Log.d(TAG, "test2: " + msg);
        textView.setText(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().ungister(this);
    }
}