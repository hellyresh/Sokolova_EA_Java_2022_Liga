package com.tasktracker.service;

import com.tasktracker.csv.CsvParser;
import com.tasktracker.csv.CsvWriter;
import com.tasktracker.model.Status;
import com.tasktracker.model.Task;
import com.tasktracker.model.User;
import com.tasktracker.repository.TaskRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class TaskService {

    private UserService userService;

    private TaskRepo taskRepo;
    @Value("${files.users}")
    private String usersFile;
    @Value("${files.tasks}")
    private String tasksFile;
    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Autowired
    public TaskService(TaskRepo taskRepo, UserService userService) {
        this.taskRepo = taskRepo;
        this.userService = userService;
    }

    public TaskService(UserService userService) {
        this.userService = userService;
    }


    public String loadData() {
        try {
            List<User> users = CsvParser.parseUsersCSV(Paths.get(usersFile));
            users.forEach(userService::addUser);
            List<Task> tasks = CsvParser.parseTasksCSV(userService, Paths.get(tasksFile));
            for (Task task : tasks) {
                addTask(task);
            }
            return "Данные загружены";
        } catch (IOException e) {
            return "Произошла ошибка";
        }
    }


    @Transactional
    public void addTask(Task task) {
        taskRepo.save(task);
    }

    public Collection<Task> getTasks() {
        return taskRepo.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException(format("Задачи с id = %d не существует", id)));
    }

    @Transactional
    public void updateTaskStatus(Task task, Status status) {
        task.setStatus(status);
        taskRepo.save(task);
    }


    public Task createTask(String header, String description, Long userId, String deadLine) {
        return createTask(header, description, userId, deadLine, "NEW");
    }

    @Transactional
    public Task createTask(String header, String description, Long userId, String deadLine, String status) {
        try {
            User user = userService.getUserById(userId);
            Task task = new Task();
            task.setHeader(header);
            task.setDescription(description);
            task.setDeadLine(LocalDate.parse(deadLine, formatter));
            task.setStatus(Status.valueOf(status));
            task.setUser(user);
            taskRepo.save(task);
            return task;
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("Ошибка ввода, статус может быть: new, in_process, done");
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Ошибка ввода, введите дату в формате: дд.мм.гггг");
        }

    }

    @Transactional
    public void updateTaskDeadline(Task task, LocalDate deadline) {
        task.setDeadLine(deadline);
        taskRepo.save(task);
    }

    @Transactional
    public void updateTasksUserId(Task task, Long userId) {
        User user = userService.getUserById(userId);
        task.setUser(user);
        taskRepo.save(task);
    }

    @Transactional
    public void updateTaskDescription(Task task, String description) {
        task.setDescription(description);
        taskRepo.save(task);
    }

    @Transactional
    public void updateTaskHeader(Task task, String header) {
        task.setHeader(header);
        taskRepo.save(task);
    }

    @Transactional
    public void deleteTaskById(Long id) {
        taskRepo.delete(getTaskById(id));
    }

    @Transactional
    public void deleteAllTasks() {
        taskRepo.deleteAll();
    }

    public String saveData() {
        try {
            CsvWriter.saveUsersToCSV(userService.getUsers(), Paths.get(usersFile));
            CsvWriter.saveTasksToCSV(getTasks(), Paths.get(tasksFile));
            return "Данные успешно сохранены";
        } catch (IOException e) {
            return e.getMessage();
        }
    }

}
