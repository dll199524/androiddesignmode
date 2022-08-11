package com.example.designmode.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public interface IDaoSupport<T> {

    void init(SQLiteDatabase database, Class<T> clazz);

    public long insert(T t);
    public void insert(List<T> datas);
    public void deleteAll();
    QuerySupport<T> querrySupport();
    int delete(String whereClause, String... whereArgs);
    int update(T obj, String whereClause, String... whereArgs);

}
