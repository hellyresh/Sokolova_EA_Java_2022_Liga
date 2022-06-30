package main.java;

import main.java.model.Task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static java.lang.String.format;

public class TaskService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final UserService userService;

    private final Map<Integer, Task> tasksById = new HashMap<>();

    public TaskService(UserService userService) {
        this.userService = userService;
    }

    public void addTask(Task task) {
        userService.linkTask(task);
        tasksById.put(task.getId(), task);
    }

    public Collection<Task> getAllTasks() {
        return tasksById.values();
    }

    public Task getTaskById(int id) {
        Task task = tasksById.get(id);
        if (task != null) {
            return task;
        }
        throw new NoSuchElementException(format("Задачи с id = %d не существует", id));
    }

    public void updateTaskStatus(Task task, Status status) throws NoSuchElementException {
        task.setStatus(status);
    }

    public Task createTask(String header, String description, int userId, LocalDate deadLine)
            throws NoSuchElementException {
        userService.getUserById(userId);
        Task task = new Task(Collections.max(tasksById.keySet()) + 1, header, description, userId, deadLine);
        tasksById.put(task.getId(), task);
        userService.linkTask(task);
        return task;
    }

    public void deleteData() {

    }

    public void deleteTask(Task task) {
        userService.unlinkTask(task);
        tasksById.remove(task.getId());
    }

    public void updateTaskDeadline(Task task, String deadline) throws DateTimeParseException {
        task.setDeadline(LocalDate.parse(deadline, formatter));
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

    public Collection<Task> getTasks(){
        return tasksById.values();
    }
}
