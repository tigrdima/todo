package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.store.CategoryStore;

import java.util.List;
import java.util.Set;

@ThreadSafe
@Service
public class CategoryService {
    private final CategoryStore categoryStore;

    public CategoryService(CategoryStore categoryStore) {
        this.categoryStore = categoryStore;
    }

    public List<Category> findAll() {
        return categoryStore.findAll();
    }

    public Category findById(int id) {
        return categoryStore.findById(id);
    }
}
