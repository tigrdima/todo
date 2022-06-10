package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;
import net.jcip.annotations.ThreadSafe;

import java.util.List;
import java.util.function.Function;

@ThreadSafe
@Repository
public class ItemStore implements SessionTodo {
    private final SessionFactory sf;

    public ItemStore(SessionFactory sf) {
        this.sf = sf;
    }

    public void add(Item item) {
        sessionApply(s -> s.save(item), sf);
    }

    public List<Item> findAll() {
        return sessionApply(s -> s.createQuery("from Item").list(), sf);
    }

    public Item findById(int id) {
        return (Item) sessionApply(s -> s
                .createQuery("from Item i where i.id = :iId")
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
        return sessionApply(s -> s.createQuery("from Item i where i.done = :iDone")
                .setParameter("iDone", true)
                .list(), sf);
    }

    public List<Item> findAllNewItems() {
       return sessionApply(s -> s.createQuery("from Item i where i.done = :iDone")
                .setParameter("iDone", false)
                .list(), sf);
    }

}
