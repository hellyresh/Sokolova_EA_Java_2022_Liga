import java.io.IOException;
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
        Parser parser = new Parser();

        List<User> users = parser.parseUsersCSV("src/resources/users.csv");
        List<Task> tasks = parser.parseTasksCSV("src/resources/tasks.csv");

        users.forEach(userService::addUser);
        tasks.forEach(taskService::addTask);
    }
}
