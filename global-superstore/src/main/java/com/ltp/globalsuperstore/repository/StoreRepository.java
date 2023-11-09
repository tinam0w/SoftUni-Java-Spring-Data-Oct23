package com.ltp.globalsuperstore.repository;

import com.ltp.globalsuperstore.model.Item;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class StoreRepository {
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
