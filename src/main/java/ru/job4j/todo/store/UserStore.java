package ru.job4j.todo.store;

import net.jcip.annotations.ThreadSafe;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;

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
        return Optional.of(user);
    }

    public Optional<User> findUserByEmailAndPwd(String email, String password) {
        Optional<User> user;
             user =  sessionApply(s -> s
                    .createQuery("from User u where u.email = :uEmail and u.password = :uPass")
                    .setParameter("uEmail", email)
                    .setParameter("uPass", password)
                    .uniqueResultOptional(), sf);
        return user;
    }

}
