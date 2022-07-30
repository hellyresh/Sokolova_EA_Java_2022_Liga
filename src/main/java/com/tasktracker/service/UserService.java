package com.tasktracker.service;

import com.tasktracker.model.Status;
import com.tasktracker.model.Task;
import com.tasktracker.model.User;
import com.tasktracker.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepo userRepo;


    public void addUser(User user) {
        userRepo.save(user);
    }


    public List<User> getUsers() {
        return userRepo.findAll();
    }

    public User getUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException(format("Пользователя с id = %d не существует", id)));

    }

    public Set<Task> getUserTasksByStatus(User user, Status status) {
        return user.getTasks().stream()
                .filter(t -> t.getStatus() == status)
                .collect(Collectors.toSet());
    }


    public void deleteAllUsers() {
        userRepo.deleteAll();
    }
}