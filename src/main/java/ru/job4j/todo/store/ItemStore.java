package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;
import net.jcip.annotations.ThreadSafe;

import java.util.List;
import java.util.function.Function;

@ThreadSafe
@Repository
public class ItemStore {
    private final SessionFactory sf;

    public ItemStore(SessionFactory sf) {
        this.sf = sf;
    }

    private <T> T sessionApply(final Function<Session, T> command) {
        Session session = sf.openSession();
        session.beginTransaction();

        T rsl = command.apply(session);

        session.getTransaction().commit();
        session.close();
        return rsl;
    }

    public Item add(Item item) {
        return (Item) sessionApply(s -> s.save(item));
    }

    public List<Item> findAll() {
        return sessionApply(s -> s.createQuery("from Item ").list());
    }

    public Item findById(int id) {
        return (Item) sessionApply(s -> s
                .createQuery("from Item i where i.id = :iId")
                .setParameter("iId", id)
                .uniqueResult());
    }

    public void update(Item item) {
        sessionApply(s -> s
                .createQuery("update Item i set i.name = :iName, i.description = :iDesc, i.done = :iDone where i.id = :iId")
                .setParameter("iName", item.getName())
                .setParameter("iDesc", item.getDescription())
                .setParameter("iDone", item.getDone())
                .setParameter("iId", item.getId())
                .executeUpdate());
    }

    public void updateDone(Item item) {
        sessionApply(s -> s
                .createQuery("update Item i set i.done = :iDone where i.id = :iId")
                .setParameter("iDone", item.getDone())
                .setParameter("iId", item.getId())
                .executeUpdate());
    }

    public void delete(Item item) {
      sessionApply(s -> s
              .createQuery("delete from Item i where i.id = :iId")
              .setParameter("iId", item.getId())
              .executeUpdate());
    }

    public List<Item> findAllCompleted() {
        return sessionApply(s -> s.createQuery("from Item i where i.done = :iDone")
                .setParameter("iDone", true)
                .list());
    }

    public List<Item> findAllNewItems() {
       return sessionApply(s -> s.createQuery("from Item i where i.done = :iDone")
                .setParameter("iDone", false)
                .list());
    }
}
