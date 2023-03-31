package com.example.designmode;




import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class ProxyActivity extends AppCompatActivity {

    private static final String TAG = ProxyActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxy);
        Log.d(TAG, "onCreate: " + this.hashCode());
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