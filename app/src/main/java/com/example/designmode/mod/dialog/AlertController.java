package com.example.designmode.mod.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


public class AlertController {

    private DialogViewHelper mViewHelper;
    private Dialog mDialog;
    private Window mWindow;

    public void setViewHelper(DialogViewHelper viewHelper) {this.mViewHelper = viewHelper;}

    protected AlertController(AlertDialog dialog, Window window) {
        this.mDialog = dialog;
        this.mWindow = window;
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

    public Dialog getDialog() {return mDialog;}
    public Window getWindow() {return mWindow;}

    static class AlertParams {
        public CharSequence mTitle;
        public Context mContext;
        public CharSequence mMessage;
        public View mView;
        public int mLayoutId;
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        public int mAnimation = 0;
        public boolean mCancle;
        public int mThemeResId;
        public int mGravity = Gravity.CENTER;


        public SparseArray<CharSequence> textArray = new SparseArray<>();
        public SparseArray<View.OnClickListener> clickArray = new SparseArray<>();

        public DialogInterface.OnCancelListener mCancleListener;
        public DialogInterface.OnKeyListener mOnKeyListener;
        public DialogInterface.OnDismissListener mOnDismissListener;


        public AlertParams(Context context, int themeId) {
            this.mContext = context;
            this.mThemeResId = themeId;
        }


        public void apply(AlertController mAlertController) {
            DialogViewHelper viewHelper = null;
            if (mLayoutId != 0)
                viewHelper = new DialogViewHelper(mContext, mLayoutId);
            if (mView != null) {
                viewHelper = new DialogViewHelper();
                viewHelper.setContentView(mView);
            }
            mAlertController.mDialog.setContentView(viewHelper.getContentView());
            mAlertController.setViewHelper(viewHelper);

            int textSize = textArray.size();
            for (int i = 0; i < textSize; i++) {
                mAlertController.setText(textArray.keyAt(i), textArray.valueAt(i));
            }

            int clickSize = clickArray.size();
            for (int i = 0; i < clickSize; i++) {
                mAlertController.setOnClickListener(clickArray.keyAt(i), clickArray.valueAt(i));
            }

            Window window = mAlertController.getWindow();
            window.setGravity(mGravity);

            WindowManager.LayoutParams params = window.getAttributes();
            params.width = mWidth;
            params.height = mHeight;
            window.setAttributes(params);

        }
    }



}
