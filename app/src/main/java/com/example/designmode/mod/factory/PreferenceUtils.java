package com.example.designmode.mod.factory;

import android.content.SharedPreferences;

public class PreferenceUtils {
    private volatile static PreferenceUtils instance;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public PreferenceUtils getInstance() {
        if (instance == null)
            synchronized (PreferenceUtils.class) {
                if (instance == null)
                    instance = new PreferenceUtils();
            }
        return instance;
    }


}
