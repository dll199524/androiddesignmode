package com.example.designmode.performance.start;

import java.util.concurrent.ConcurrentHashMap;

public class StartUpCacheManager {

    private ConcurrentHashMap<Class<? extends StartUp>, Result> initializedComponents =
            new ConcurrentHashMap<>();
    private static volatile StartUpCacheManager instance;

    private StartUpCacheManager() {}
    public static StartUpCacheManager getInstance() {
        if (instance == null) {
            synchronized (StartUpCacheManager.class) {
                if (instance == null)
                    instance = new StartUpCacheManager();
            }
        }
        return instance;
    }

    public void saveInitializedComponet(Class<? extends StartUp> clazz, Result result) {initializedComponents.put(clazz, result);}
    public boolean hasInitialized(Class<? extends StartUp> key) {return initializedComponents.containsKey(key);}
    public <T> Result<T> obtainInitializedComponet(Class<? extends StartUp<T>> clazz) {return initializedComponents.get(clazz);}
    public void remove(Class<? extends StartUp> clazz) {initializedComponents.remove(clazz);}
    public void clear() {initializedComponents.clear();}

}
