package com.example.designmode.aop;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.tbruyelle.rxpermissions3.RxPermissions;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

//定义切面 切点

@Aspect
public class CheckUtils {

    private final String TAG = CheckUtils.class.getSimpleName();
    final String CHECK_POINT = "execution(@com.example.designmode.aop * *..())";
    @Pointcut(CHECK_POINT)
    public void checkBehaviorNet() {}

    @Pointcut(CHECK_POINT)
    public void checkBehaviorPermission() {}

    @Around("checkBehaviorNet()")
    public Object checkNet(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.d(TAG, "checkNet: -------------");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CheckNet checkNet = signature.getMethod().getAnnotation(CheckNet.class);
        if (checkNet != null) {
            Object obj = joinPoint.getThis();
            Context context = getContext(obj);
            if (context != null) {
                if (!isNetWorkAvailable(context))
                    return null;
            }
        }

        return joinPoint.proceed();
    }

    @Around("checkBehaviorPermission()")
    public Object checkPermission(ProceedingJoinPoint joinPoint, CheckPermission permission) throws Throwable {
        Log.d(TAG, "checkPermission: -------------");
        Context context = getContext(joinPoint.getThis());
        if (context != null) {
            RxPermissions rxPermissions = new RxPermissions((FragmentActivity) context);
            rxPermissions.request(permission.value())
                    .subscribe(granted -> {
                       if (granted) joinPoint.proceed();
                       else {
                           Toast.makeText(context, "权限申请失败", Toast.LENGTH_SHORT).show();
                       }
                    });
        }

        return joinPoint.proceed();
    }





    private boolean isNetWorkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
            if (networkInfos != null && networkInfos.length > 0) {
                for (int i = 0; i < networkInfos.length; i++)
                    if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) return true;
            }
        }
        return false;
    }

    private Context getContext(Object obj) {
        if (obj instanceof Activity) return (Activity) obj;
        else if (obj instanceof Fragment) {
            Fragment fragment = (Fragment) obj;
            return fragment.getActivity();
        } else if (obj instanceof View) {
            View view = (View) obj;
            return view.getContext();
        }
        return null;
    }

}
