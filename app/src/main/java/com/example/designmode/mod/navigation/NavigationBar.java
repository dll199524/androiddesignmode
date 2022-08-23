package com.example.designmode.mod.navigation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.designmode.R;

public class NavigationBar extends AbsNavigationBar <NavigationBar.Bulider>{

    protected NavigationBar(Bulider bulider) {
        super(bulider);
    }

    @Override
    public void addParams() {
        super.addParams();
        TextView textView = (TextView) findViewById(R.id.back_tv);
        textView.setVisibility(getBulider().viewVisibility);
    }

    public static class Bulider extends AbsNavigationBar.Bulider <NavigationBar.Bulider> {

        public int viewVisibility = View.VISIBLE;
        public Bulider(Context context, ViewGroup parent) {
            super(context, R.layout.activity_main, parent);
        }
        public Bulider(Context context, int layoutId, ViewGroup parent) {
            super(context, layoutId, parent);
        }

        @Override
        public AbsNavigationBar create() {
            return new NavigationBar(this);
        }

        public Bulider hideToLeft() {
            viewVisibility = View.GONE;
            return this;
        }
    }
}
