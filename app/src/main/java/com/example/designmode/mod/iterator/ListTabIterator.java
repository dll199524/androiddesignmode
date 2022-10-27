package com.example.designmode.mod.iterator;

import java.util.ArrayList;
import java.util.List;

public class ListTabIterator<T extends MainBottomTabItem> implements TabIterator{
    private List<T> items;
    private int index = 0;
    public ListTabIterator() {
        items = new ArrayList<>();
    }

    public void addItem(T item) {items.add(item);}

    @Override
    public boolean hasNext() {
        return index < items.size();
    }

    @Override
    public BottomTabItem next() {
        return items.get(index++);
    }
}
