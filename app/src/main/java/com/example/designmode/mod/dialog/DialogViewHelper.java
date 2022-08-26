package com.example.designmode.mod.dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DialogViewHelper {
    private View mContentView = null;
    private final SparseArray<WeakReference<View>> mViews = new SparseArray<>();
    public DialogViewHelper(Context context, int layoutId) {
        mContentView = LayoutInflater.from(context).inflate(layoutId, null);
    }

    public void setText(int viewId, CharSequence text) {
        View view = getView(viewId);
        if (view != null)
            try {
                Class clazz = view.getClass();
                Method setTextMethod = clazz.getMethod("setText", CharSequence.class);
                setTextMethod.invoke(view, text);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

    }

    //减少findViewById的调用
    public <T extends View> T getView(int viewId) {
        WeakReference<View> viewReference = mViews.get(viewId);
        View view = null;
        if (viewReference != null)
            view = viewReference.get();
        if (view == null) {
            view = mContentView.findViewById(viewId);
            mViews.put(viewId, new WeakReference<>(view));
        }
        return (T) view;
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        if (view != null)
            view.setOnClickListener(listener);
    }

    public void setContentView(View contentView) {this.mContentView = contentView;}

    public View getContentView() {return  mContentView;}

}
