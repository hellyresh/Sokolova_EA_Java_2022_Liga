package com.tasktracker.commands;


import com.tasktracker.model.Status;
import com.tasktracker.model.User;
import com.tasktracker.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
@AllArgsConstructor
public class ShowUserTasks implements Command {

    @Autowired
    private UserService userService;

    @Override
    public Object execute(List<String> args) {

        if (args.size() < 1) {
            return "Некорректное количество аргументов";
        }
        try {
            User user = userService.getUserById(Integer.parseInt((args.get(0))));
            if (args.size() <= 1) {
                return user.getTasks();
            }

            Status status = Status.valueOf(args.get(1).toUpperCase());
            return userService.getUserTasksByStatus(user, status);
        } catch (NoSuchElementException | IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @Override
    public String getTitle() {
        return "show_user_tasks";
    }

    @Override
    public String getManual() {
        return """
                Команда для вывода задач пользователя <br> <br>
                Аргументы команды отделяются запятой <br> <br>
                Шаблон команды: show_user_tasks, id пользователя, [статус] <br> <br>
                Статус может принимать значения: new, in_process, done <br> <br>
                Статус - опциональный параметр, будут выведены задачи, отфильтрованные по статусу. Если его не
                 указывать - будут выведены все задачи пользователя <br>
                <br>
                Пример использования: <br>
                http://localhost:8080/cli?command=show_user_tasks, 1, in_process <br>
                http://localhost:8080/cli?command=show_user_tasks, 2
                """;
    }
}