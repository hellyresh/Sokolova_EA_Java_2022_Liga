package com.tasktracker.services;

import com.tasktracker.csv.CsvParser;
import com.tasktracker.csv.CsvWriter;
import com.tasktracker.model.Status;
import com.tasktracker.model.Task;
import com.tasktracker.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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

    private final Map<Integer, Task> tasksById = new HashMap<>();

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Value("${files.users}")
    private String usersFile;
    @Value("${files.tasks}")
    private String tasksFile;


    @PostConstruct
    private void loadData() throws IOException {
        List<User> users = CsvParser.parseUsersCSV(Paths.get(usersFile));
        List<Task> tasks = CsvParser.parseTasksCSV(Paths.get(tasksFile));

        users.forEach(userService::addUser);
        for (Task task : tasks) {
            addTask(task);
        }
    }

    public void addTask(Task task) {
        userService.linkTask(task);
        tasksById.put(task.getId(), task);
    }

    public Collection<Task> getTasks() {
        return tasksById.values();
    }

    public Task getTaskById(int id) {
        Task task = tasksById.get(id);
        if (task != null) {
            return task;
        }
        throw new NoSuchElementException(format("Задачи с id = %d не существует", id));
    }

    public void updateTaskStatus(Task task, String status) {
        task.setStatus(Status.valueOf(status.toUpperCase()));
    }

    public Task createTask(String header, String description, int userId, String deadLine) {
        return createTask(header, description, userId, deadLine, NEW.name());
    }

    public Task createTask(String header, String description, int userId, String deadLine, String status)
            throws NoSuchElementException {

        userService.getUserById(userId);

        Task task = new Task(Collections.max(tasksById.keySet()) + 1,
                header,
                description,
                userId,
                LocalDate.parse(deadLine, formatter),
                Status.valueOf(status.toUpperCase()));
        addTask(task);
        return task;
    }

    public void updateTaskDeadline(Task task, String deadline) {
        try {
            task.setDeadLine(LocalDate.parse(deadline, formatter));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Ошибка ввода, введите дату в формате: дд.мм.гггг", e);
        }
    }

    public void updateTasksUserId(Task task, int userId) throws NoSuchElementException {
        userService.getUserById(userId);
        userService.unlinkTask(task);
        task.setUserId(userId);
        userService.linkTask(task);
    }

    public void updateTaskDescription(Task task, String description) {
        task.setDescription(description);
    }

    public void updateTaskHeader(Task task, String header) {
        task.setHeader(header);
    }

    public void deleteTaskById(int id) {
        Task task = getTaskById(id);
        userService.unlinkTask(task);
        tasksById.remove(id);
    }

    public void deleteAllTasks() {
        tasksById.clear();
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
