package ru.job4j.todo.store;

import net.jcip.annotations.ThreadSafe;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ThreadSafe
@Repository
public class CategoryStore implements SessionTodo {
    private final SessionFactory sf;

    public CategoryStore(SessionFactory sf) {
        this.sf = sf;
    }

    public List<Category> findAll() {
        return sessionApply(s -> s
                .createQuery("from Category").list(), sf);
    }

    public Category findById(int id) {
        return (Category) sessionApply(s -> s
                .createQuery("from Category c where c.id = :cId")
                .setParameter("cId", id)
                .uniqueResult(), sf);
    }
}
