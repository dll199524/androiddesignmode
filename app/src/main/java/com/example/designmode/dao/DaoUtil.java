package com.example.designmode.dao;

import android.text.TextUtils;
import android.util.Log;

import java.util.Locale;

public class DaoUtil<T> {

    private static String TAG = "DaoUtil";

    private DaoUtil() {
        throw new UnsupportedOperationException("can not be instantiated");
    }

    public static String getTableName(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    public static String getColumnType(String type) {
        String value = null;
        if (type.contains("String"))
            value = "text";
        else if (type.contains("int"))
            value = "integer";
        else if (type.contains("boolean"))
            value = "boolean";
        else if (type.contains("float"))
            value = "float";
        else if (type.contains("char"))
            value = "varchar";
        else if (type.contains("double"))
            value = "double";
        else if (type.contains("long"))
            value = "long";
        else
            Log.d(TAG, "getColumnType: invalid value");
        return value;
    }

    public static String capitalize(String str) {
        if (!TextUtils.isEmpty(str))
            return str.substring(0, 1).toUpperCase(Locale.US) + str.substring(1);
        return str == null ? null : "";
    }
}
