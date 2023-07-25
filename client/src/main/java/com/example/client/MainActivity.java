package com.example.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.example.aidl.IOptions;
import java.io.File;


//intent to transfer big file
public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";
    private IOptions options;
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            options = IOptions.Stub.asInterface(iBinder);
            transferData();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindToService();
    }

    private void bindToService() {
        Intent intent = new Intent();
        intent.setAction("com.example.aidl.ServerService");
        intent.setClassName("com.example.server", "com.example.server.ServerService");
        boolean bac = bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        Log.d(TAG, "bindToService: " + bac);
    }

    private void transferData() {
        try {
            ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(new File(getCacheDir(), "file.iso"),
                    ParcelFileDescriptor.MODE_READ_ONLY);
            options.transactFileDescriptor(fileDescriptor);
            fileDescriptor.close();
        } catch (Exception e) {e.printStackTrace();}

    }


}