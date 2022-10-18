package com.example.designmode.mod.adapter;

import android.view.View;
import android.view.ViewGroup;

public interface AdapterTarget {

    int getCount();
    View getView(int position, ViewGroup parent);

}
