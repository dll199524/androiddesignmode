package com.example.designmode.mod.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.designmode.R;

import java.util.List;

//对象适配
public class MyListViewAdapter implements AdapterTarget{

    private List<String> items;
    private Context context;

    public MyListViewAdapter(List<String> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View getView(int position, ViewGroup parent) {
        TextView view = (TextView) LayoutInflater.from(context).inflate(R.layout.listadapter_view, parent, false);
        view.setText(items.get(position));
        return view;
    }
}
