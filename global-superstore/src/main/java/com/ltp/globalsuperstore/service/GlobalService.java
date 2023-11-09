package com.ltp.globalsuperstore.service;

import com.ltp.globalsuperstore.model.Item;
import com.ltp.globalsuperstore.repository.GlobalRepository;
import com.ltp.globalsuperstore.utils.Constants;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GlobalService {

    GlobalRepository globalRepository = new GlobalRepository();

    public String submitItem(Item item){
        int index = getIndexFromId(item.getId());
        String status = Constants.SUCCESS_STATUS;
        if (index == Constants.NOT_FOUND) {
            globalRepository.addItem(item);
        } else if (within5Days(item.getDate(), globalRepository.getItemByIndex(index).getDate())) {
            globalRepository.updateItem(index, item);
        } else {
            status = Constants.FAILED_STATUS;
        }
        return status;
    }

    public Item getItemById(String id){
        int index = getIndexFromId(id);
        return index == Constants.NOT_FOUND ? new Item() : globalRepository.getItemByIndex(index);
    }

    public int getIndexFromId(String id) {
        for (int i = 0; i < globalRepository.getItems().size(); i++) {
            if (globalRepository.getItemByIndex(i).getId().equals(id)) return i;
        }
        return Constants.NOT_FOUND;
    }

    public boolean within5Days(Date newDate, Date oldDate) {
        long diff = Math.abs(newDate.getTime() - oldDate.getTime());
        return (int) (TimeUnit.MILLISECONDS.toDays(diff)) <= 5;
    }

    public List<Item> getItems(){
        return globalRepository.getItems();
    }
}
