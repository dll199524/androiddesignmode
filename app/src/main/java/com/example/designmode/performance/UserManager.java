package com.example.designmode.performance;

import android.content.Context;

public class UserManager {

    private static UserManager instance;

    private Context context;

    private UserManager(Context context) {
        this.context = context;
    }

    public static UserManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserManager(context);
        }
        return instance;
    }
}
