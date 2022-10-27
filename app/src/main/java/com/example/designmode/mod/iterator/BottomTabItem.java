package com.example.designmode.mod.iterator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public abstract class BottomTabItem {
    private Context mContext;
    private int mLayoutId;
    private View mTabView;

    public BottomTabItem(Context mContext, int mLayoutId) {
        this.mContext = mContext;
        this.mLayoutId = mLayoutId;
    }

    public View getTabView() {
        if (mTabView == null) {
            mTabView = LayoutInflater.from(mContext).inflate(mLayoutId, null);
            initLayout();
        }
        return mTabView;
    }

    protected abstract void initLayout();

    protected <T> T findViewById(int id) {return (T)mTabView.findViewById(id);}

    abstract void selected(boolean selected);

}
