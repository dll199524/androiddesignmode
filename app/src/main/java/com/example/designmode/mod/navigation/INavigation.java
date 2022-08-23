package com.example.designmode.mod.navigation;

import android.view.View;
import android.view.ViewGroup;

public interface INavigation {

    void createNavigationBar();

    void attachParent(View navigationBar, ViewGroup parent);

    void addParams();
}
