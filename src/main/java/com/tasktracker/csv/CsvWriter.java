package com.tasktracker.csv;

import com.tasktracker.model.Task;
import com.tasktracker.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Component
public class CsvWriter {

    public static void saveUsersToCSV(Collection<User> users, Path filePath) throws IOException {
        Files.writeString(filePath, "");
        for (User user : users) {
            Files.writeString(filePath, user.toCsvRow(), StandardOpenOption.APPEND);
        }
    }

    public static void saveTasksToCSV(Collection<Task> tasks, Path filePath) throws IOException {
        Files.writeString(filePath, "");
        for (Task task : tasks) {
            Files.writeString(filePath, task.toCsvRow(), StandardOpenOption.APPEND);
        }
    }
}
