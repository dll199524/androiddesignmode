package com.example.designmode.mod.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class MyListView extends ScrollView {


    private AdapterTarget adapter;
    private LinearLayout container;

    public MyListView(Context context) {
        this(context, null);
    }

    public MyListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        container = new LinearLayout(context);
        container.setOrientation(LinearLayout.VERTICAL);
    }

    @Override
    public void addView(View child) {
        container.addView(child);
    }

    public void setAdapter(MyListViewAdapter listViewAdapter) {
        this.adapter = listViewAdapter;
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            addView(adapter.getView(i, container));
        }
    }

}
