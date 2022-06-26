import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class UI {

    private final UserService userService;
    private final TaskService taskService;

    private final Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

    UI(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    public void runUI() {
        boolean isRunning = true;
        while (isRunning) {
            showStartMenu();
            switch (scanner.nextLine()) {
                case "1" -> showUsers();
                case "2" -> showUserTasks();
                case "3" -> changeTaskStatus();
                case "4" -> isRunning = false;
                default -> System.out.println("\nВведите существующую команду\n");
            }
        }
    }

    public void showStartMenu() {
        System.out.println("""
                Введите номер команды:
                1. Показать список пользователей
                2. Показать задачи пользователя
                3. Сменить статус задачи
                4. Завершить работу""");
    }

    public void showUsers() {
        System.out.println("\nid\tИмя");
        userService.getAllUsers().forEach(System.out::println);
        System.out.println();
    }

    private void showUserTasks() {
        System.out.println("\nВведите id пользователя: ");
        User user;
        try {
            user = userService.getUserById(parseInt(scanner.nextLine()));
        }
        catch (NumberFormatException e) {
            System.out.println("Неверный формат ввода\n");
            return;
        }
        catch (NoSuchElementException e) {
            System.out.println(e.getMessage() + "\n");
            return;
        }

        System.out.println("""
                Выберите статус задач:
                1. NEW
                2. IN_PROCESS
                3. DONE
                Введите любое другое значение, чтобы показать все задачи""");

        String command = scanner.nextLine();
        System.out.println("\nЗадачи пользователя " + user.getName() + ":\n");
        switch (command) {
            case "1" -> showUserTasksByStatus(user, Status.NEW);
            case "2" -> showUserTasksByStatus(user, Status.IN_PROCESS);
            case "3" -> showUserTasksByStatus(user, Status.DONE);
            default -> user.getTasks().forEach(System.out::println);
        }
    }

    private void showUserTasksByStatus(User user, Status status) {
        user.getTasks().stream()
                .filter(task -> task.getStatus().equals(status))
                .forEach(System.out::println);
    }

    private void changeTaskStatus() {
        System.out.println("\nВведите id задачи: ");
        int taskId;
        try {
            taskId = parseInt(scanner.nextLine());
            System.out.println(taskService.getTaskById(taskId));
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат ввода\n");
            return;
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage() + "\n");
            return;
        }


        System.out.println("""
                Выберите новый статус задачи:
                1. NEW
                2. IN_PROCESS
                3. DONE
                Введите любое другое значение, чтобы отменить""");

        switch (scanner.nextLine()) {
            case "1" -> taskService.updateTaskStatus(taskId, Status.NEW);
            case "2" -> taskService.updateTaskStatus(taskId, Status.IN_PROCESS);
            case "3" -> taskService.updateTaskStatus(taskId, Status.DONE);
        }
    }
}
