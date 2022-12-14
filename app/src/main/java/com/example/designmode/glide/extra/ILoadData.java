package com.example.designmode.glide.extra;

import android.content.Context;

import com.example.designmode.glide.resource.Value;

public interface ILoadData {
    Value loadResource(String path, ResponseListener listener, Context context);
}
