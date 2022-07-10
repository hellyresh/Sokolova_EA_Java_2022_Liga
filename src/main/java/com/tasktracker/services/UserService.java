package com.tasktracker.services;

import com.tasktracker.model.Status;
import com.tasktracker.model.Task;
import com.tasktracker.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Map<Integer, User> usersById = new HashMap<>();

    public void addUser(User user) {
        usersById.put(user.getId(), user);
    }

    public void linkTask(Task task) {
        getUserById(task.getUserId()).getTasks().add(task);
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

    public Set<Task> getUserTasksByStatus(User user, Status status) {
        return user.getTasks().stream()
                .filter(t -> t.getStatus() == status)
                .collect(Collectors.toSet());
    }

    public void unlinkTask(Task task) {
        getUserById(task.getUserId()).getTasks().remove(task);
    }

    public void deleteAllUsers() {
        usersById.clear();
    }
}
