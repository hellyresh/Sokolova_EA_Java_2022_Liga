package main.java.model;

import main.java.Status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final int id;
    private String header;
    private String description;
    private int userId;
    private LocalDate deadline;
    private Status status;

    public Task(int id, String header, String description, int userId, LocalDate deadline) {
        this.id = id;
        this.header = header;
        this.description = description;
        this.userId = userId;
        this.deadline = deadline;
        this.status = Status.NEW;
    }

    public Task(int id, String header, String description, int userId, LocalDate deadline, Status status) {
        this.id = id;
        this.header = header;
        this.description = description;
        this.userId = userId;
        this.deadline = deadline;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String toString() {
        return "id задачи        \t" + id + "\n" +
                "заголовок       \t" + header + "\n" +
                "описание        \t" + description + "\n" +
                "id пользователя \t" + userId + "\n" +
                "дедлайн         \t" + deadline.format(formatter) + "\n" +
                "статус          \t" + status + "\n";
    }


    public void setHeader(String header) {
        this.header = header;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public String toCsvRow() {
        return id + ", " + header + ", " + description + ", " + userId + ", " +
                deadline.format(formatter) + ", " + status + "\n";
    }
}
