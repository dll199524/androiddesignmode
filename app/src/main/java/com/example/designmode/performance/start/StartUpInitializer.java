package com.example.designmode.performance.start;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.os.Message;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartUpInitializer {
    public static String META_VALUE = "android.startup";

    public static List<StartUp<?>> discoverAndInitializ(Context context, String providerName)
        throws Exception {
        Map<Class<? extends StartUp>, StartUp<?>> map = new HashMap<>();
        ComponentName provider = new ComponentName(context, providerName);
        ProviderInfo providerInfo = context.getPackageManager().getProviderInfo(provider,
                PackageManager.GET_META_DATA);
        for (String key : providerInfo.metaData.keySet()) {
            String val = providerInfo.metaData.getString(key);
            if (TextUtils.equals(META_VALUE, val)) {
                Class<?> clazz = Class.forName(key);
                if (StartUp.class.isAssignableFrom(clazz))
                    doInitialize((StartUp<?>) clazz.newInstance(), map);
            }
        }
        List<StartUp<?>> result = new ArrayList<>(map.values());
        return result;
    }

    public static void doInitialize(StartUp<?> startUp, Map<Class<? extends StartUp>, StartUp<?>> map) throws IllegalAccessException, InstantiationException {
        map.put(startUp.getClass(), startUp);
        if (startUp.getDependiceCount() != 0) {
            for (Class<? extends StartUp<?>> parent : startUp.dependencies())
                doInitialize(parent.newInstance(), map);
        }
    }
}
