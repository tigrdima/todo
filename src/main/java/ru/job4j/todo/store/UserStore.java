package ru.job4j.todo.store;

import net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;
import java.util.function.Function;

@ThreadSafe
@Repository
public class UserStore implements SessionTodo {
    private final SessionFactory sf;

    public UserStore(SessionFactory sf) {
        this.sf = sf;
    }

    public Optional<User> add(User user) {
        try {
            sessionApply(s -> s.save(user), sf);
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.ofNullable(user);
    }

    public Optional<User> findUserByEmailAndPwd(String email, String password) {
        User user;
        try {
            user = (User) (sessionApply(s -> s
                    .createQuery("from User u where u.email = :uEmail and u.password = :uPass")
                    .setParameter("uEmail", email)
                    .setParameter("uPass", password)
                    .uniqueResult(), sf));
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.ofNullable(user);
    }

}
