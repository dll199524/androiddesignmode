package com.example.designmode.mod.factory;

public interface IOHandler {

    void save(String key, String val);
    void save(String key, double val);
    void save(String key, int val);
    void save(String key, long val);
    void save(String key, boolean val);
    void save(String key, Object val);

    String getString(String key);
    double getDouble(String key, double defaultVal);
    int getInt(String key, int defaultVal);
    long getLong(String key, long defaultVal);
    boolean getBoolean(String key, boolean defaultVal);
    Object getObject(String key, Object defaultVal);

}
