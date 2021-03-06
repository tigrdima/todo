package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import net.jcip.annotations.ThreadSafe;

import java.time.Instant;
import java.util.List;
import java.util.function.Function;

@ThreadSafe
@Repository
public class ItemStore implements SessionTodo {
    private final SessionFactory sf;

    public ItemStore(SessionFactory sf) {
        this.sf = sf;
    }

    public void add(Item item, List<String> categoryId) {
        sessionApply(s -> {
                for (String id : categoryId) {
                    Category category = (Category) s.createQuery("from Category c where id = :cId")
                            .setParameter("cId", Integer.parseInt(id))
                            .uniqueResult();
                    item.getCategories().add(category);
                }
            s.save(item);
            return item;
        }, sf);
    }

    public List<Item> findAll() {
        return sessionApply(s -> s.createQuery("select distinct i from Item i join fetch i.categories").list(), sf);
    }

    public Item findById(int id) {
        return (Item) sessionApply(s -> s
                .createQuery("select distinct i from Item i join fetch i.categories where i.id = :iId")
                .setParameter("iId", id)
                .uniqueResult(), sf);
    }

    public void update(Item item) {
        sessionApply(s -> s
                .createQuery("update Item i set i.name = :iName, i.description = :iDesc, i.done = :iDone where i.id = :iId")
                .setParameter("iName", item.getName())
                .setParameter("iDesc", item.getDescription())
                .setParameter("iDone", item.getDone())
                .setParameter("iId", item.getId())
                .executeUpdate(), sf);
    }

    public void updateDone(Item item) {
        sessionApply(s -> s
                .createQuery("update Item i set i.done = :iDone where i.id = :iId")
                .setParameter("iDone", item.getDone())
                .setParameter("iId", item.getId())
                .executeUpdate(), sf);
    }

    public void delete(Item item) {
        sessionApply(s -> s
                .createQuery("delete from Item i where i.id = :iId")
                .setParameter("iId", item.getId())
                .executeUpdate(), sf);
    }

    public List<Item> findAllCompleted() {
        return sessionApply(s -> s.createQuery("select distinct i from Item i join fetch i.categories where i.done = :iDone")
                .setParameter("iDone", true)
                .list(), sf);
    }

    public List<Item> findAllNewItems() {
        return sessionApply(s -> s.createQuery("select distinct i from Item i join fetch i.categories where i.done = :iDone")
                .setParameter("iDone", false)
                .list(), sf);
    }

}
