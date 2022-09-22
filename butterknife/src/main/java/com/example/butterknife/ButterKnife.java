package com.example.butterknife;

import android.app.Activity;

import java.lang.reflect.Constructor;

public class ButterKnife {

    public static Unbinder bind(Activity activity) {
        if (activity == null) throw new IllegalArgumentException("activity should not be null");
        try {
            Class<? extends Unbinder> bindName = (Class<? extends Unbinder>) Class.forName(activity.getClass().getName()
            + "_viewBinding");
            Constructor<? extends Unbinder> bindConstructor = bindName.getDeclaredConstructor(activity.getClass());
            return bindConstructor.newInstance(activity);
        } catch (Exception e) {e.printStackTrace();}
        return Unbinder.EMPTY;
    }
}
