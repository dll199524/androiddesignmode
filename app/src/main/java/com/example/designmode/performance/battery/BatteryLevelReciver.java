package com.example.designmode.performance.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

public class BatteryLevelReciver extends BroadcastReceiver {

    private volatile float batteryPct;
    private int level;
    private int scale;
    private boolean isCharging;
    private int voltage;
    private int chargingType;


    @Override
    public void onReceive(Context context, Intent intent) {
        //当前剩余电量
        level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        //最大电量
        scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
        int status = intent.getIntExtra("status", 0);
        isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
    }

    public int getLevel() {
        return level;
    }

    public int getScale() {
        return scale;
    }

    public boolean isCharging() {
        return isCharging;
    }

    public int getVoltage() {
        return voltage;
    }
}
