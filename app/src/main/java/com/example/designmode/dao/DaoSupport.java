package com.example.designmode.dao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.ArrayMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class DaoSupport<T> implements IDaoSupport<T>{

    SQLiteDatabase mSqLiteDatabase;
    Class<T> mClazz;
    QuerySupport<T> mQuerrySupport;
    private static final Map<String, Method> mPutMethod = new ArrayMap();

    public DaoSupport(SQLiteDatabase sqLiteDatabase, Class<T> clazz) {
        init(sqLiteDatabase, clazz);
    }

    @Override
    public void init(SQLiteDatabase database, Class<T> clazz) {
        this.mSqLiteDatabase = database;
        this.mClazz = clazz;
        // 创建表
        /*"create table if not exists Person ("
                + "id integer primary key autoincrement, "
                + "name text, "
                + "age integer, "
                + "flag boolean)";*/
        StringBuilder sb = new StringBuilder();
        sb.append("create table if not exists ")
                .append(DaoUtil.getTableName(mClazz))
                .append("(id integer primary key autoincrement, ");
        Field[] fields = mClazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            String type = field.getType().getSimpleName();
            sb.append(name).append(DaoUtil.getColumnType(type)).append(", ");
        }
        sb.replace(sb.length() - 2, sb.length(), ")");
        mSqLiteDatabase.execSQL(sb.toString());
    }

    @Override
    public long insert(Object obj) {
        /*
        通常我们可能直接调用
        ContentValues values = new ContentValues();
        values.put("name","wz");
        values.put("author","xx");
        values.put("price",1.0);
        db.insert("Book",null,values);

        但是这样其实不方便 如果我们删除或增加Book.java的字段 那么这段代码需要修改
        并且 对于不同的对象 我们还需要写不同的插入逻辑，利用反射 就可以直接绕开这两个问题
        因此 这里我们使用了反射
        */
        ContentValues values = ContentValuesByObj(obj);
        return mSqLiteDatabase.insert(DaoUtil.getTableName(mClazz), null, values);
    }


    @Override
    public void insert(List<T> datas) {
        mSqLiteDatabase.beginTransaction();
        for (T data : datas)
            insert(data);
        mSqLiteDatabase.setTransactionSuccessful();
        mSqLiteDatabase.endTransaction();
    }

    @Override
    public void deleteAll() {
        mSqLiteDatabase.execSQL("DROP TABLE" + DaoUtil.getTableName(mClazz));
    }

    @Override
    public QuerySupport querrySupport() {
        if (mQuerrySupport == null)
            mQuerrySupport = new QuerySupport<>(mSqLiteDatabase, mClazz);
        return mQuerrySupport;
    }

    @Override
    public int delete(String whereClause, String... whereArgs) {
        return mSqLiteDatabase.delete(DaoUtil.getTableName(mClazz), whereClause, whereArgs);
    }

    @Override
    public int update(Object obj, String whereClause, String... whereArgs) {
        ContentValues values = ContentValuesByObj(obj);
        return mSqLiteDatabase.update(DaoUtil.getTableName(mClazz), values, whereClause, whereArgs);
    }



    private ContentValues ContentValuesByObj(Object obj) {
        ContentValues contentValues = new ContentValues();
        Field[] fields = mClazz.getDeclaredFields();
        for (Field field : fields) {
            Method putMethod;
            try {
                field.setAccessible(true);
                String key = field.getName();
                Object value = field.get(obj);
                String fileTypeName = field.getType().getName();
                putMethod = mPutMethod.get(fileTypeName);
                if (putMethod == null) {
                    putMethod = ContentValues.class.getDeclaredMethod("put", String.class, value.getClass());
                    mPutMethod.put(fileTypeName, putMethod);
                }
                putMethod.invoke(contentValues, key, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return contentValues;
    }
}
