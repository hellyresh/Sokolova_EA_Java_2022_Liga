package main.java;

import main.java.model.Task;
import main.java.model.User;

import java.util.*;

import static java.lang.String.format;

public class UserService {

    private final Map<Integer, User> usersById = new HashMap<>();

    public void addUser(User user) {
        usersById.put(user.getId(), user);
    }

    public void linkTask(Task task) {
        getUserById(task.getUserId()).addTask(task);
    }

    public Collection<User> getUsers() {
        return usersById.values();
    }

    public User getUserById(int id) {
        User user = usersById.get(id);
        if (user != null) {
            return user;
        }
        throw new NoSuchElementException(format("Пользователя с id = %d не существует", id));
    }

    public void unlinkTask(Task task) {
        getUserById(task.getUserId()).getTasks().remove(task);
    }

    public void deleteAllUsers() {
        usersById.clear();
    }
}
