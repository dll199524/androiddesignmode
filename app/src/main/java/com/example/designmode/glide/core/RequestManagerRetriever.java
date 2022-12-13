package com.example.designmode.glide.core;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.designmode.glide.binding.ApplicationLifeCycle;
import com.example.designmode.glide.binding.RequestManagerFragment;
import com.example.designmode.glide.binding.SupportRequestManagerFragment;
import com.example.designmode.glide.utils.Util;

import java.util.HashMap;
import java.util.Map;

//管理RequestManager
public class RequestManagerRetriever implements Handler.Callback {

    static final String FRAGMENT_TAG = "com.example.designmode.glide";
    private Handler handler;
    private Map<android.app.FragmentManager, RequestManagerFragment> pendingRequestManagerFragment =
            new HashMap<>();
    private Map<FragmentManager, SupportRequestManagerFragment> pendingSupportRequestManagerFragment =
            new HashMap<>();
    private static final int FRAGMENT_MANAGER = 1; //android app fragment
    private static final int SUPPORT_FRAGMENT_MANAGER = 2; //androidx fragment
    private RequestManager applicationManager;
    public RequestManagerRetriever() {
        handler = new Handler(Looper.getMainLooper(), this);
    }

    public RequestManager get(Context context) {
        if (context == null) throw new IllegalArgumentException("context is null....");
        else if (Util.isOnMainThread() && !(context instanceof Application)) {
            if (context instanceof FragmentActivity) return get((FragmentActivity) context);
            else if (context instanceof Activity) return get((Activity) context);
            else if (context instanceof ContextWrapper
                    && ((ContextWrapper) context).getBaseContext().getApplicationContext() != null)
                return get(((ContextWrapper) context).getBaseContext());
        }
        return getApplicationManager(context);
    }

    private RequestManager getApplicationManager(Context context) {
        if (applicationManager == null) {
            synchronized (this) {
                if (applicationManager == null) {
                    Glide glide = Glide.get(context);
                    applicationManager = new RequestManager(glide, new ApplicationLifeCycle(), context);
                }
            }
        }
        return applicationManager;
    }


    public RequestManager get(FragmentActivity activity) {
        if (Util.isOnBackgroundThread()) {
            return get(activity.getApplicationContext());
        } else {
            FragmentManager fm = activity.getSupportFragmentManager();
            return supportFragmentGet(activity, fm);
        }
    }

    public RequestManager get(Fragment fragment) {
        if (Util.isOnBackgroundThread()) {
            return get(fragment.getContext().getApplicationContext());
        } else {
            FragmentManager fm = fragment.getChildFragmentManager();
            return supportFragmentGet(fragment.getContext(), fm);
        }
    }

    public RequestManager get(Activity activity) {
        if (Util.isOnBackgroundThread()) {
            return get(activity);
        } else {
            android.app.FragmentManager fm = activity.getFragmentManager();
            return fragmentGet(activity, fm);
        }
    }

    //android app fragment
    private RequestManager fragmentGet(Context context, android.app.FragmentManager fm) {
        RequestManagerFragment current = getRequestManagerFragment(fm);
        RequestManager requestManager = current.getRequestManager();
        if (requestManager == null) {
            Glide glide = Glide.get(context);
            requestManager = new RequestManager(glide, current.getGlideLifeCycle(), context);
            current.setRequestManager(requestManager);
        }
        return requestManager;
    }

    private RequestManagerFragment getRequestManagerFragment(android.app.FragmentManager fm) {
        RequestManagerFragment current = (RequestManagerFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (current == null) {
            current = pendingRequestManagerFragment.get(fm);
            if (current == null) {
                current = new RequestManagerFragment();
                pendingRequestManagerFragment.put(fm, current);
                fm.beginTransaction().add(current, FRAGMENT_TAG).commitAllowingStateLoss();
                handler.obtainMessage(FRAGMENT_MANAGER, fm).sendToTarget();
            }
        }
        return current;
    }

    //fragment
    private RequestManager supportFragmentGet(Context context, FragmentManager fm) {
        SupportRequestManagerFragment current = getSupportRequestManagerFragment(fm);
        RequestManager requestManager = current.getRequestManager();
        if (requestManager == null) {
            Glide glide = Glide.get(context);
            requestManager = new RequestManager(glide, current.getGlideLifeCycle(), context);
            current.setRequestManager(requestManager);
        }
        return requestManager;
    }

    private SupportRequestManagerFragment getSupportRequestManagerFragment(FragmentManager fm) {
        SupportRequestManagerFragment current = (SupportRequestManagerFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (current == null) {
            current = pendingSupportRequestManagerFragment.get(fm);
            if (current == null) {
                current = new SupportRequestManagerFragment();
                pendingSupportRequestManagerFragment.put(fm, current);
                handler.obtainMessage(SUPPORT_FRAGMENT_MANAGER, fm).sendToTarget();
            }
        }
        return current;
    }


    @Override
    public boolean handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case FRAGMENT_MANAGER:
                break;
            case SUPPORT_FRAGMENT_MANAGER:
                break;
        }
        return false;
    }
}
