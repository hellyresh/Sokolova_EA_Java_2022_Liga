package hellyresh;

import hellyresh.csv.CsvParser;
import hellyresh.model.Task;
import hellyresh.model.User;
import hellyresh.services.TaskService;
import hellyresh.services.UserService;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        UserService userService = new UserService();
        TaskService taskService = new TaskService(userService);
        initialize(userService, taskService);
        UI userInterface = new UI(userService, taskService);
        userInterface.runUI();
    }

    private static void initialize(UserService userService, TaskService taskService) throws IOException {
        CsvParser parser = new CsvParser();

        List<User> users = parser.parseUsersCSV(Paths.get("src/main/resources/users.csv"));
        List<Task> tasks = parser.parseTasksCSV(Paths.get("src/main/resources/tasks.csv"));

        users.forEach(userService::addUser);
        tasks.forEach(taskService::addTask);
    }
}
