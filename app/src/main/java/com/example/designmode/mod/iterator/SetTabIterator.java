package com.example.designmode.mod.iterator;

import android.util.ArraySet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetTabIterator<T extends MainBottomTabItem> implements TabIterator{

    private Set<T> items;
    private int index = 0;
    public SetTabIterator() {
        items = new HashSet<>();
    }

    public void addItem(T item) {items.add(item);}

    @Override
    public boolean hasNext() {
        return index < items.size();
    }

    @Override
    public BottomTabItem next() {
        return items.iterator().next();
    }
}
