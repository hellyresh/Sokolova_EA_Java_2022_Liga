package com.tasktracker.services;

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

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static com.tasktracker.model.Status.NEW;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final UserService userService;
    @Autowired
    private TaskRepo taskRepo;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Value("${files.users}")
    private String usersFile;
    @Value("${files.tasks}")
    private String tasksFile;



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
            return e.getMessage();
        }
    }

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

    public void updateTaskStatus(Task task, String status) {
        task.setStatus(Status.valueOf(status.toUpperCase()));
    }

    public Task createTask(String header, String description, Long userId, String deadLine) {
        return createTask(header, description, userId, deadLine, NEW.name());
    }

    public Task createTask(String header, String description, Long userId, String deadLine, String status) {
        User user = userService.getUserById(userId);
        Task task = new Task();
        task.setHeader(header);
        task.setDescription(description);
        task.setDeadLine(LocalDate.parse(deadLine, formatter));
        task.setStatus(Status.valueOf(status.toUpperCase()));
        task.setUser(user);
        addTask(task);
        return task;
    }

    public void updateTaskDeadline(Task task, String deadline) {
        try {
            task.setDeadLine(LocalDate.parse(deadline, formatter));
            taskRepo.save(task);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Ошибка ввода, введите дату в формате: дд.мм.гггг", e);
        }
    }

    public void updateTasksUserId(Task task, Long userId) {
        User user = userService.getUserById(userId);
        task.setUser(user);
        taskRepo.save(task);
    }

    public void updateTaskDescription(Task task, String description) {
        task.setDescription(description);
        taskRepo.save(task);
    }

    public void updateTaskHeader(Task task, String header) {
        task.setHeader(header);
        taskRepo.save(task);
    }

    public void deleteTaskById(Long id) {
        taskRepo.delete(getTaskById(id));
    }

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
