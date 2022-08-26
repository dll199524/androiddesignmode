package com.example.designmode.mod.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.View;


public class AlertController {

    private DialogViewHelper mViewHelper;

    public void setViewHelper(DialogViewHelper viewHelper) {this.mViewHelper = viewHelper;}

    protected AlertController(Context context, int themeId) {

    }

    public void setText(int viewId, CharSequence text) {
        mViewHelper.setText(viewId, text);
    }

    public <T extends View> T getView(int viewId) {
        return mViewHelper.getView(viewId);
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        mViewHelper.setOnClickListener(viewId, listener);
    }

    static class AlertParams {
        public CharSequence mTitle;
        public Context mContext;
        public CharSequence mMessage;
        public View mView;
        public int mLayoutId;
        public int mWidth;
        public int mHeight;
        public int mAnimation;
        public boolean mCancle;

        public SparseArray<CharSequence> textArray = new SparseArray<>();
        public SparseArray<View.OnClickListener> clickArray = new SparseArray<>();

        public DialogInterface.OnCancelListener mCancleListener;
        public DialogInterface.OnKeyListener mOnKeyListener;
        public DialogInterface.OnDismissListener mOnDismissListener;
    }



}
