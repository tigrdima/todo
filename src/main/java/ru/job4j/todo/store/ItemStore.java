package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.*;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;
import net.jcip.annotations.ThreadSafe;

import java.util.List;

@ThreadSafe
@Repository
public class ItemStore {
    private final SessionFactory sf;

    public ItemStore(SessionFactory sf) {
        this.sf = sf;
    }

    public Item add(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();

        session.save(item);

        session.getTransaction().commit();
        session.close();
        return item;
    }

    public List<Item> findAll() {
        Session session = sf.openSession();
        session.beginTransaction();

        List rsl = session
                .createQuery("from Item")
                .list();

        session.getTransaction().commit();
        session.close();
        return rsl;
    }

    public Item findById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();

        Item item = (Item) session
                .createQuery("from Item i where i.id = :iId")
                .setParameter("iId", id)
                .uniqueResult();

        session.getTransaction().commit();
        session.close();

        return item;
    }

    public void update(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();

        session
                .createQuery("update Item i set i.name = :iName, i.description = :iDesc, i.done = :iDone where i.id = :iId")
                .setParameter("iName", item.getName())
                .setParameter("iDesc", item.getDescription())
                .setParameter("iDone", item.getDone())
                .setParameter("iId", item.getId())
                .executeUpdate();

        session.getTransaction().commit();
        session.close();
    }

    public void updateDone(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();

        session
                .createQuery("update Item i set i.done = :iDone where i.id = :iId")
                .setParameter("iDone", item.getDone())
                .setParameter("iId", item.getId())
                .executeUpdate();

        session.getTransaction().commit();
        session.close();
    }

    public void delete(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();

        session.delete(item);

        session.getTransaction().commit();
        session.close();
    }

    public List<Item> findAllCompleted() {
        Session session = sf.openSession();
        session.beginTransaction();

        List rsl = session.createQuery("from Item i where i.done = :iDone")
                .setParameter("iDone", true)
                .list();

        session.getTransaction().commit();
        session.close();
        return rsl;
    }

    public List<Item> findAllNewItems() {
        Session session = sf.openSession();
        session.beginTransaction();

        List rsl = session.createQuery("from Item i where i.done = :iDone")
                        .setParameter("iDone", false)
                                .list();

        session.getTransaction().commit();
        session.close();

        return rsl;
    }
}
