package com.example.designmode.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import java.io.File;


public class DaoSupportFactroy {

    private static String TAG = "DaoSupportFactroy";
    private SQLiteDatabase mSqLiteDatabase;
    private static volatile DaoSupportFactroy instance;

    public DaoSupportFactroy(Context context) {
        File dbroot = new File(context.getExternalFilesDir(null)
                .getAbsoluteFile() + File.separator + "nhdz" + File.separator + "database");
        if (!dbroot.exists())
            dbroot.mkdirs();
        File dbfile = new File(dbroot, "outcheck.db");
        mSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbfile, null);

    }

    public DaoSupportFactroy getInstance(Context context) {
        if (instance == null) {
            synchronized (DaoSupportFactroy.class) {
                instance = new DaoSupportFactroy(context);
            }
        }
        return instance;
    }

    public <T>IDaoSupport<T> getDao(Class<T> clazz) {return new DaoSupport<T>(mSqLiteDatabase, clazz);}

}
