package com.example.designmode.mod.iterator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TabItemNavigation extends LinearLayout {

    List<BottomTabItem> items;
    private int currentIndex = 0;
    public TabItemNavigation(Context context) {
        this(context, null);
    }

    public TabItemNavigation(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabItemNavigation(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        items = new ArrayList<>();
    }

    public void addTabItem(List<BottomTabItem> bottomTabItems) {
        items.clear();
        items.addAll(bottomTabItems);
        for (int i = 0; i < bottomTabItems.size(); i++) {
            BottomTabItem bottomTabItem = bottomTabItems.get(i);
            View tabView = bottomTabItem.getTabView();
            addView(tabView);

            LinearLayout.LayoutParams params = (LayoutParams) tabView.getLayoutParams();
            params.weight = 1;
            params.gravity = Gravity.CENTER;
            tabView.setLayoutParams(params);
            setItemClickListener(tabView, i);
        }
        items.get(0).selected(true);
    }

    private void setItemClickListener(View tabView, int position) {
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != currentIndex) {
                    items.get(currentIndex).selected(false);
                    currentIndex = position;
                    items.get(currentIndex).selected(true);

                    //回调position切换页面
                }
            }
        });
    }
}
