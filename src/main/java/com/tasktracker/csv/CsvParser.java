package com.tasktracker.csv;

import com.tasktracker.model.Status;
import com.tasktracker.model.Task;
import com.tasktracker.model.User;
import com.tasktracker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

@Component
public class CsvParser {

    private static final int USER_ID_INDEX = 0;
    private static final int USER_NAME_INDEX = 1;
    private static final int TASK_ID_INDEX = 0;
    private static final int TASK_HEADER_INDEX = 1;
    private static final int TASK_DESCRIPTION_INDEX = 2;
    private static final int TASK_USER_ID_INDEX = 3;
    private static final int TASK_DEADLINE_INDEX = 4;
    private static final int TASK_STATUS_INDEX = 5;
    private static final Status DEFAULT_STATUS = Status.NEW;
    private static final String DELIMITER = ",";


    private static UserService userService;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static List<User> parseUsersCSV(Path filePath) throws IOException {
        try (Stream<String> lines = Files.lines(filePath)) {
            return lines.map(it -> it.split(DELIMITER))
                    .map(it -> new User(parseLong(it[USER_ID_INDEX]), it[USER_NAME_INDEX].trim(), new HashSet<>()))
                    .toList();
        }
    }

    public static List<Task> parseTasksCSV(UserService userService, Path filePath) throws IOException {
        try (Stream<String> lines = Files.lines(filePath)) {
            return lines.map(it -> it.split(DELIMITER))
                    .map(it -> new Task(parseLong(it[TASK_ID_INDEX]),
                            it[TASK_HEADER_INDEX].trim(),
                            it[TASK_DESCRIPTION_INDEX].trim(),
                            getUser(userService, parseLong(it[TASK_USER_ID_INDEX].trim())),
                            LocalDate.parse(it[TASK_DEADLINE_INDEX].trim(), formatter),
                            getStatus(it))
                    )
                    .toList();
        }
    }

    private static User getUser(UserService userService, Long userId) {
        return userService.getUserById(userId);
    }

    private static Status getStatus(String[] it) {
        if (it.length > TASK_STATUS_INDEX) {
           return Status.valueOf(it[TASK_STATUS_INDEX].trim().toUpperCase());
        }
        return DEFAULT_STATUS;
    }
}
