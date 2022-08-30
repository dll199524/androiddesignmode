package com.example.designmode.mod.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;

import com.example.designmode.R;


public class AlertDialog extends Dialog implements DialogInterface {

    private AlertController mAlertController;
    protected AlertDialog(Context context, int themeId) {
        super(context, themeId);
        mAlertController = new AlertController(this, getWindow());
    }

    public void setText(int viewId, CharSequence text) {
        mAlertController.setText(viewId, text);
    }

    public <T extends View> T getView(int viewId) {return mAlertController.getView(viewId);}

    public void setOnClickListener(int viewId, View.OnClickListener listener) {mAlertController.setOnClickListener(viewId, listener);}

    public static class Bulider {

        private AlertController.AlertParams params;

        public Bulider(Context context) {this(context, R.style.dialog);}

        public Bulider(Context context, int themeId) {params = new AlertController.AlertParams(context, themeId);}

        public Bulider setTitle(CharSequence title) {
            params.mTitle = title;
            return this;
        }
        public Bulider setTitle(int titleId) {
            params.mTitle = params.mContext.getText(titleId);
            return this;
        }
        public Bulider setMessage(int messageId) {
            params.mMessage = params.mContext.getText(messageId);
            return this;
        }

        public Bulider setText(int viewId, CharSequence text) {
            params.textArray.put(viewId, text);
            return this;
        }

        public Bulider setContentView(View view) {
            params.mView = view;
            params.mLayoutId = 0;
            return this;
        }

        public Bulider setContentView(int layoutId) {
            params.mView = null;
            params.mLayoutId = layoutId;
            return this;
        }

        public Bulider setWidthAndHeight(int width, int height) {
            params.mWidth = width;
            params.mHeight = height;
            return this;
        }

        public Bulider fullWith() {
            params.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        public Bulider defaultAnimation() {
            params.mAnimation = R.style.dialog_default_anim;
            return this;
        }

        public Bulider fromButtom(boolean isAnimation) {
            if (isAnimation) params.mAnimation = R.style.dialog_from_bootom_anim;
            return this;
        }


        public Bulider setAnimation(int animation) {
            params.mAnimation = animation;
            return this;
        }

        public Bulider setCancle(boolean cancle) {
            params.mCancle = cancle;
            return this;
        }

        public Bulider setOnCancleListener(DialogInterface.OnCancelListener listener) {
            params.mCancleListener = listener;
            return this;
        }

        public Bulider setOnKeyListener(DialogInterface.OnKeyListener listener) {
            params.mOnKeyListener = listener;
            return this;
        }

        public Bulider setOnDismissListener(DialogInterface.OnDismissListener listener) {
            params.mOnDismissListener = listener;
            return this;
        }


        public AlertDialog create() {
            final AlertDialog dialog = new AlertDialog(params.mContext, params.mThemeResId);
            params.apply(dialog.mAlertController);
            dialog.setCancelable(params.mCancle);
            if (params.mCancle) dialog.setCancelable(true);
            dialog.setOnCancelListener(params.mCancleListener);
            dialog.setOnDismissListener(params.mOnDismissListener);
            if (params.mOnKeyListener != null) dialog.setOnKeyListener(params.mOnKeyListener);
            return dialog;
        }

        public AlertDialog show() {
            final AlertDialog dialog = create();
            dialog.show();
            return dialog;
        }


    }

}
