package main.java;

import main.java.model.Task;
import main.java.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

//TODO: переименовать (?)
public class WriterToCSV {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public void saveUsersToCSV(Collection<User> users, Path filePath) throws IOException {
        Files.writeString(filePath, "");
        for (User user :  users) {
            Files.writeString(filePath, user.toCsvRow(), StandardOpenOption.APPEND);
        }
    }

    public void saveTasksToCSV(Collection<Task> tasks, Path filePath) throws IOException {
        Files.writeString(filePath, "");
        for (Task task :  tasks) {
            Files.writeString(filePath, task.toCsvRow(), StandardOpenOption.APPEND);
        }
    }


}
