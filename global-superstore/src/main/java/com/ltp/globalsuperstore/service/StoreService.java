package com.ltp.globalsuperstore.service;

import com.ltp.globalsuperstore.model.Item;
import com.ltp.globalsuperstore.repository.StoreRepository;
import com.ltp.globalsuperstore.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class StoreService {
    StoreRepository storeRepository;

    @Autowired
    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public String submitItem(Item item){
        int index = getIndexFromId(item.getId());
        String status = Constants.SUCCESS_STATUS;
        if (index == Constants.NOT_FOUND) {
            storeRepository.addItem(item);
        } else if (within5Days(item.getDate(), storeRepository.getItemByIndex(index).getDate())) {
            storeRepository.updateItem(index, item);
        } else {
            status = Constants.FAILED_STATUS;
        }
        return status;
    }

    public Item getItemById(String id){
        int index = getIndexFromId(id);
        return index == Constants.NOT_FOUND ? new Item() : storeRepository.getItemByIndex(index);
    }

    public int getIndexFromId(String id) {
        for (int i = 0; i < storeRepository.getItems().size(); i++) {
            if (storeRepository.getItemByIndex(i).getId().equals(id)) return i;
        }
        return Constants.NOT_FOUND;
    }

    public boolean within5Days(Date newDate, Date oldDate) {
        long diff = Math.abs(newDate.getTime() - oldDate.getTime());
        return (int) (TimeUnit.MILLISECONDS.toDays(diff)) <= 5;
    }

    public List<Item> getItems(){
        return storeRepository.getItems();
    }
}
