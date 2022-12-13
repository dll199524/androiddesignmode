package com.example.designmode.glide.resource;

import android.graphics.Bitmap;

public class Value {
    private Bitmap bitmap;
    private String key;
    private ValueCallback callback;

    public Bitmap getBitmap() {return bitmap;}

    public void setBitmap(Bitmap bitmap) {this.bitmap = bitmap;}

    public void setKey(String key) {this.key = key;}

    public void setCallback(ValueCallback callback) {this.callback = callback;}

    public void recycle() {
        if (callback != null) callback.onValueNotUseListener(key, this);
    }
}
