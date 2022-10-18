package com.example.designmode.mod.director;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class WrapRecyclerView extends RecyclerView {

    private HeaderViewRecycleAdapter mAdapter;
    public WrapRecyclerView(@NonNull Context context) {
        super(context);
    }

    public WrapRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        mAdapter = new HeaderViewRecycleAdapter(adapter);
        super.setAdapter(mAdapter);
    }

    public void addHeadView(View view) {if (mAdapter != null) mAdapter.addHeadView(view);}
    public void removeHeadView(View view) {if (mAdapter != null) mAdapter.removeHeadView(view);}
    public void addFootView(View view) {if (mAdapter != null) mAdapter.addFootView(view);}
    public void removeFootView(View view) {if (mAdapter != null) mAdapter.removeFootView(view);}


//    装饰设计模式：继承的一种替换方式不采用继承的情况下使类变得越来越强大
//    io流 contextWrapper listview的adapter

}