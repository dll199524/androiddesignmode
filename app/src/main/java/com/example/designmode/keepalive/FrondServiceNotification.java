package com.example.designmode.keepalive;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.designmode.MainActivity;
import com.example.designmode.R;


public class FrondServiceNotification {

    public static void start(Service service) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("demo", "demo",
                    NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = (NotificationManager) service.getSystemService(
                    Context.NOTIFICATION_SERVICE);
            if (manager == null) {
                return;
            }
            manager.createNotificationChannel(channel);
        }

        Intent notificationIntent = new Intent(service, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(service, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(service, "demo")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(service.getString(service.getApplicationInfo().labelRes) + " 正在运行")
                .setContentText("请不要强制退出应用，避免异常")
                .setAutoCancel(false)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setOngoing(true)
                .setPriority(NotificationManager.IMPORTANCE_LOW)
                .build();
        notification.flags |= Notification.FLAG_NO_CLEAR;
        service.startForeground(10, notification);
    }
}
