package com.example.designmode.mod.iterator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.designmode.R;

import java.util.ArrayList;
import java.util.List;

public class BotttomTabActivity extends AppCompatActivity {

    TabItemNavigation tabItemNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_botttom_tab);
        tabItemNavigation = findViewById(R.id.bottom_tab);

        List<BottomTabItem> items = new ArrayList<>();
//        items.add(new MainBottomTabItem(this));
//        items.add(new MainBottomTabItem(this));
//        items.add(new MainBottomTabItem(this));
        items.add(new MainBottomTabItem.Bulider(this).resId(R.drawable.main_tab_item).text("text1").create());
        items.add(new MainBottomTabItem.Bulider(this).resId(R.drawable.main_tab_item).text("text2").create());
        items.add(new MainBottomTabItem.Bulider(this).resId(R.drawable.main_tab_item).text("text3").create());
        tabItemNavigation.addTabItem(items);
    }
}