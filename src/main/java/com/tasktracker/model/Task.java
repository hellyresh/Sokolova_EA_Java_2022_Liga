package com.tasktracker.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Getter
@Setter
@AllArgsConstructor
public class Task {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final int id;
    private String header;
    private String description;
    private int userId;
    private LocalDate deadLine;
    private Status status;

    public String toCsvRow() {
        return id + ", " + header + ", " + description + ", " + userId + ", " +
                deadLine.format(formatter) + ", " + status + "\n";
    }
}
