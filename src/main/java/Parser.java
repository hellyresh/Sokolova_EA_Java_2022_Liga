package main.java;

import main.java.model.Task;
import main.java.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.lang.Integer.parseInt;

public class Parser {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public List<User> parseUsersCSV(Path filePath) throws IOException {
        return Files.lines(filePath)
                .map(it -> it.split(","))
                .map(it -> new User(parseInt(it[0]), it[1].trim()))
                .toList();
    }

    public List<Task> parseTasksCSV(Path filePath) throws IOException {
        return Files.lines(filePath)
                .map(it -> it.split(","))
                .map(it -> new Task(parseInt(it[0]),
                        it[1].trim(),
                        it[2].trim(),
                        parseInt(it[3].trim()),
                        LocalDate.parse(it[4].trim(), formatter))
                )
                .toList();
    }

}
