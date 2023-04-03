package com.example.designmode.performance.start;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;


public class StartUpProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        try {
            List<StartUp<?>> startUps = StartUpInitializer
                    .discoverAndInitializ(getContext(), getClass().getName());
            new StartUpManager.Bulider()
                    .addAllStartUp(startUps)
                    .bulider(getContext())
                    .start()
                    .aWait();
        } catch (Exception e) {e.printStackTrace();}


        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
