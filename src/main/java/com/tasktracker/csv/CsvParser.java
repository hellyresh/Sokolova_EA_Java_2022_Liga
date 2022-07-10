package com.tasktracker.csv;

import com.tasktracker.model.Status;
import com.tasktracker.model.Task;
import com.tasktracker.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

@Component
public class CsvParser {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static List<User> parseUsersCSV(Path filePath) throws IOException {
        try(Stream<String> lines = Files.lines(filePath)) {
            return lines.map(it -> it.split(","))
                    .map(it -> new User(parseInt(it[0]), it[1].trim()))
                    .toList();
        }
    }

    public static List<Task> parseTasksCSV(Path filePath) throws IOException {
        try(Stream<String> lines = Files.lines(filePath)) {
            return lines.map(it -> it.split(","))
                    .map(it -> new Task(parseInt(it[0]),
                            it[1].trim(),
                            it[2].trim(),
                            parseInt(it[3].trim()),
                            LocalDate.parse(it[4].trim(), formatter),
                            it.length > 5 ? Status.valueOf(it[5].trim().toUpperCase()) : Status.NEW)
                    )
                    .toList();
        }
    }
}
