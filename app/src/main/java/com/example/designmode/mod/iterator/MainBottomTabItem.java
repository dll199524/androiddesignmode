package com.example.designmode.mod.iterator;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.designmode.R;

import org.w3c.dom.Text;

public class MainBottomTabItem extends BottomTabItem {

    private Bulider bulider;

    private MainBottomTabItem(Context mContext) {
        super(mContext, R.layout.main_top_bottom_item);
    }

    public MainBottomTabItem(Bulider bulider) {
        this(bulider.mContext);
        this.bulider = bulider;
    }

    @Override
    protected void initLayout() {
        TextView tabText = findViewById(R.id.tab_tv);
        ImageView tabIcon = findViewById(R.id.tab_img);
        if (!TextUtils.isEmpty(bulider.mText)) {tabText.setText(bulider.mText);}
        if (bulider.resId != 0) {tabIcon.setImageResource(bulider.resId);}
    }

    @Override
    void selected(boolean selected) {
        TextView tabText = findViewById(R.id.tab_tv);
        ImageView tabIcon = findViewById(R.id.tab_img);
        tabText.setSelected(selected);
        tabIcon.setSelected(selected);
    }

    public static class Bulider {
        Context mContext;
        String mText;
        int resId;
        public Bulider (Context context) {this.mContext = context;}
        public Bulider text(String text) {
            this.mText = text;
            return this;
        }
        public Bulider resId(int resId) {
            this.resId = resId;
            return this;
        }
        public MainBottomTabItem create() {return new MainBottomTabItem(this);}

    }
}
