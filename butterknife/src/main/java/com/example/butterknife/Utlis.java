package com.example.butterknife;

import android.app.Activity;
import android.view.View;

public class Utlis {
    public static <T extends View> T findViewById(Activity activity, int viewId) {return activity.findViewById(viewId);}
}
