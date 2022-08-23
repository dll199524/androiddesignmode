package com.example.designmode.mod.navigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public abstract class AbsNavigationBar<T extends AbsNavigationBar.Bulider> implements INavigation{

    private T mBulider;
    private View mNavigationBar;

    protected AbsNavigationBar(T bulider) {
        this.mBulider = bulider;
        createNavigationBar();
    }

    public T getBulider() {return mBulider;}

    @Override
    public void createNavigationBar() {
        mNavigationBar = LayoutInflater.from(mBulider.mContext).inflate(mBulider.mLayoutId, null);
        attachParent(mNavigationBar, mBulider.mParent);
        addParams();
    }

    @Override
    public void attachParent(View navigationBar, ViewGroup parent) {
        parent.addView(navigationBar, 0);
    }

    @Override
    public void addParams() {
        Map<Integer, CharSequence> textMap = mBulider.mTextMap;
        for (Map.Entry<Integer, CharSequence> entry : textMap.entrySet()) {
            TextView view = mNavigationBar.findViewById(entry.getKey());
            view.setText(entry.getValue());
        }

        Map<Integer, View.OnClickListener> clickListenerMap = mBulider.mClickListenerMap;
        for (Map.Entry<Integer, View.OnClickListener> entry : clickListenerMap.entrySet()) {
            View view = mNavigationBar.findViewById(entry.getKey());
            view.setOnClickListener(entry.getValue());
        }
    }

    public <T extends View> T findViewById(int id) {
        return mNavigationBar.findViewById(id);
    }


    public static abstract class Bulider <T extends Bulider> {
        public Context mContext;
        public int mLayoutId;
        public ViewGroup mParent;
        public Map<Integer, CharSequence> mTextMap;
        public Map<Integer, View.OnClickListener> mClickListenerMap;

        public Bulider(Context context, int layoutId, ViewGroup parent) {
            this.mContext = context;
            this.mLayoutId = layoutId;
            this.mParent = parent;
            mTextMap = new HashMap<>();
            mClickListenerMap = new HashMap<>();
        }

        public abstract AbsNavigationBar create();
        public T setText(int id, CharSequence text) {
            mTextMap.put(id, text);
            return (T) this;
        }
        public T setOnClickListener(int id, View.OnClickListener listener) {
            mClickListenerMap.put(id, listener);
            return (T) this;
        }

    }
}
