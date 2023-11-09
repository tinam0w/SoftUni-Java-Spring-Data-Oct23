package com.ltp.globalsuperstore.repository;

import com.ltp.globalsuperstore.model.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GlobalRepository {
    private List<Item> items = new ArrayList<>();

    public Item getItemByIndex(int index) {
        return items.get(index);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void updateItem(int index, Item item) {
        items.set(index, item);
    }

    public List<Item> getItems(){
        return Collections.unmodifiableList(items);
    }
}
