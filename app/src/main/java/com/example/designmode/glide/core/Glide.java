package com.example.designmode.glide.core;

import android.content.Context;


public class Glide {


    private static Glide glide;
    private RequestManagerRetriever requestManagerRetriever;
    public static RequestManager with(Context context) {
        return getRetriever(context).get(context);
    }

    private static RequestManagerRetriever getRetriever(Context context) {
        return Glide.get(context).getRequestManagerRetriever();
    }

    private RequestManagerRetriever getRequestManagerRetriever() {return requestManagerRetriever;}

    public static Glide get(Context context) {
        if (glide == null)
            glide = new Glide();
        return glide;
    }
}
