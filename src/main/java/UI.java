package main.java;

import main.java.model.Task;
import main.java.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class UI {

    private UserService userService;
    private TaskService taskService;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

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
                case "3" -> editTask();
                case "4" -> createTask();
                case "5" -> deleteTask();
                case "6" -> {
                    deleteData();
                    isRunning = false;
                }
                case "7" -> isRunning = false;
                default -> System.out.println("\nВведите существующую команду\n");
            }
        }

    }

    private void deleteTask() {
        System.out.println("\nВведите id задачи: ");
        try {
            Task task = taskService.getTaskById(parseInt(scanner.nextLine()));
            System.out.println(task);
            System.out.println("Введите 1, чтобы удалить задачу, " +
                    "введите любое другое значение, чтобы отменить");
            if ("1".equals(scanner.nextLine())) {
                taskService.deleteTask(task);
                System.out.println("Задача " + task.getId() + " удалена");
            }
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат ввода\n");
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage() + "\n");
        }
    }

    private void deleteData() {
        System.out.println("Введите 1, чтобы удалить все задачи и пользователей, " +
                "введите любое другое значение, чтобы отменить");
        if (scanner.nextLine().equals("1")) {
            //TODO: добавить удаление в файлах
            taskService = null;
            userService = null;
            System.gc();
            System.out.println("\nДанные удалены\n");
        }

    }



    private void editTask() {
        System.out.println("\nВведите id задачи: ");
        try {
            Task task = taskService.getTaskById(parseInt(scanner.nextLine()));
            boolean isEdit = true;
            while (isEdit) {
                System.out.println(task);
                System.out.println("""                
                        Выберите поле для изменения:
                        1. Заголовок
                        2. Описание
                        3. Идентификатор пользователя
                        4. Дедлайн
                        5. Статус
                        6. Завершить редактирование""");
                switch (scanner.nextLine()) {
                    case "1" -> changeTaskHeader(task);
                    case "2" -> changeTaskDescription(task);
                    case "3" -> changeTasksUserId(task);
                    case "4" -> changeTaskDeadline(task);
                    case "5" -> changeTaskStatus(task);
                    case "6" -> isEdit = false;
                    default -> System.out.println("\nВведите существующую команду\n");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат ввода\n");
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage() + "\n");
        } catch (DateTimeParseException e) {
            System.out.println("Неверный формат даты\n");
        }
    }

    private void changeTaskDeadline(Task task) throws DateTimeParseException {
        System.out.print("Введите новую дату дедлайна: ");
        taskService.updateTaskDeadline(task, scanner.nextLine());
        System.out.println("Дедлайн установлен:");
    }

    private void changeTasksUserId(Task task) throws NoSuchElementException {
        System.out.print("Введите новый идентификатор пользователя: ");
        taskService.updateTasksUserId(task, Integer.parseInt(scanner.nextLine()));
        System.out.println("Идентификатор пользователя установлен:");
    }

    private void changeTaskDescription(Task task) {
        System.out.print("Введите новое описание задачи: ");
        taskService.updateTaskDescription(task, scanner.nextLine());
        System.out.println("Описание задачи установлено:");
    }

    private void changeTaskHeader(Task task) {
        System.out.print("Введите новый заголовок: ");
        taskService.updateTaskHeader(task, scanner.nextLine());
        System.out.println("Заголовок установлен:");
    }

    private void createTask() {
        try {
            System.out.print("Введите параметры задачи" + "\n" +
                    "Заголовок: ");
            String header = scanner.nextLine();
            System.out.print("Описание: ");
            String description = scanner.nextLine();
            System.out.print("Идентификатор пользователя: ");
            int userId = Integer.parseInt(scanner.nextLine());
            System.out.print("Дедлайн: ");
            LocalDate deadLine = LocalDate.parse(scanner.nextLine(), formatter);
            Task task = taskService.createTask(header, description, userId, deadLine);
            System.out.println("\n" + "Задача добавлена:\n\n" + task);
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат ввода\n");
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage() + "\n");
        } catch (DateTimeParseException e) {
            System.out.println("Неверный формат даты\n");
        }
    }

    public void showStartMenu() {
        System.out.println("""
                
                Введите номер команды:
                1. Показать список пользователей
                2. Показать задачи пользователя
                3. Редактировать задачу
                4. Добавить новую задачу
                5. Удалить задачу
                6. Удалить все задачи и пользователей
                7. Завершить работу""");
    }

    public void showUsers() {
        System.out.println("\nid\tИмя");
        userService.getUsers().forEach(System.out::println);
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

    private void changeTaskStatus(Task task) {
        System.out.println("""
                Выберите новый статус задачи:
                1. NEW
                2. IN_PROCESS
                3. DONE
                Введите любое другое значение, чтобы отменить""");

        switch (scanner.nextLine()) {
            case "1" -> taskService.updateTaskStatus(task, Status.NEW);
            case "2" -> taskService.updateTaskStatus(task, Status.IN_PROCESS);
            case "3" -> taskService.updateTaskStatus(task, Status.DONE);
            default -> {return;}
        }
        System.out.println("Статус задачи установлен:");
    }
}
