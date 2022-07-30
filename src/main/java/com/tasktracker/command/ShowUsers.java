package com.tasktracker.command;


import com.tasktracker.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@AllArgsConstructor
public class ShowUsers implements Command {

    @Autowired
    private UserService userService;

    @Override
    public Object execute(List<String> args) {
        return userService.getUsers();
    }

    @Override
    public String getTitle() {
        return "show_users";
    }

    @Override
    public String getManual() {
        return """
                Команда для вывода всех пользователей <br>
                Пример использования: http://localhost:8080/cli?command=show_users
                """;
    }
}
