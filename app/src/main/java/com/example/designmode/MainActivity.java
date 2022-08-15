package com.example.designmode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.designmode.performance.UserManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserManager userManager = UserManager.getInstance(this);
    }
}