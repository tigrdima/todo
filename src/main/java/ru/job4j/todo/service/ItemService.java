package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.store.ItemStore;


import java.util.List;

@ThreadSafe
@Service
public class ItemService {
    private final ItemStore itemStore;

    public ItemService(ItemStore itemStore) {
        this.itemStore = itemStore;
    }

    public void add(Item item) {
        itemStore.add(item);
    }

    public List<Item> findAll() {
        return itemStore.findAll();
    }

    public Item findById(int id) {
        return itemStore.findById(id);
    }

    public void update(Item item) {
        itemStore.update(item);
    }

    public void updateDone(Item item) {
        itemStore.updateDone(item);
    }

    public void delete(Item item) {
        itemStore.delete(item);
    }

    public List<Item> findAllCompleted() {
        return itemStore.findAllCompleted();
    }

    public List<Item> findAllNewItems() {
        return itemStore.findAllNewItems();
    }
}
