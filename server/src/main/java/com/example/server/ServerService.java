package com.example.server;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;


import com.example.aidl.IOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ServerService extends Service {


    private final static String TAG = "ServerService";
    private final static String CHANNEL_ID_String = "com.example.ServerService";
    private final static int CHANNLE_ID = 0X01;
    private IOptions.Stub options = new IOptions.Stub() {
        @Override
        public void transactFileDescriptor(ParcelFileDescriptor pfd) throws RemoteException {
            Log.d(TAG, "transactFileDescriptor: currentThread" + Thread.currentThread().getName());
            Log.d(TAG, "transactFileDescriptor: calling pid = " + Binder.getCallingPid() + Binder.getCallingUid());
            File file = new File(getCacheDir(), "file.iso");
            try {
                ParcelFileDescriptor.AutoCloseInputStream inputStream = new ParcelFileDescriptor.AutoCloseInputStream(pfd);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                inputStream.close();
            } catch (IOException e) {e.printStackTrace();}
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return options;
    }

    public ServerService() {}

    @Override
    public void onCreate() {
        super.onCreate();
        startServiceForeground();
    }

    private void startServiceForeground() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_String, "Server", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(getApplicationContext(),
                    CHANNEL_ID_String).build();
            startForeground(CHANNLE_ID, notification);
        } else {
            Log.d(TAG, "startServiceForeground: must up android O");
        }
    }
}
