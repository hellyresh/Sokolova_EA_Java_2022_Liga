package com.tasktracker.command;

import com.tasktracker.service.TaskService;
import com.tasktracker.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class DeleteAllUsersAndTasks implements Command {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Override
    public String execute(List<String> args) {
        taskService.deleteAllTasks();
        userService.deleteAllUsers();
        return "Данные удалены, чтобы очистить csv файлы не забудьте сохранить изменения.";
    }

    @Override
    public String getTitle() {
        return "delete_all";
    }

    @Override
    public String getManual() {
        return """
                Команда для очистки таск трекера <br>
                Для того, чтобы очистить файлы - сохраните изменения командой save_data <br> <br>
                Пример использования: <br>
                http://localhost:8080/cli?command=delete_all
                """;
    }
}
