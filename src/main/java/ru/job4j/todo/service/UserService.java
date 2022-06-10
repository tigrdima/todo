package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.UserStore;

import java.util.Optional;

@ThreadSafe
@Service
public class UserService {
    private final UserStore userStore;

    public UserService(UserStore userStore) {
        this.userStore = userStore;
    }

    public Optional<User> add(User user) {
       return userStore.add(user);
    }

    public Optional<User> findUserByEmailAndPwd(String email, String password) {
        return userStore.findUserByEmailAndPwd(email, password);
    }

}
