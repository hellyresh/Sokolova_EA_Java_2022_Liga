package com.tasktracker.commands;

import com.tasktracker.services.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.Integer.parseInt;

@Component
@AllArgsConstructor
public class DeleteTask implements Command {

    private final int ID_INDEX = 0;
    private final int ARGS_COUNT = 1;

    @Autowired
    private TaskService taskService;


    @Override
    public String execute(List<String> args) {
        if (args.size() != ARGS_COUNT) {
            return "Некорректное количество аргументов";
        }

        try {
            taskService.deleteTaskById(parseInt(args.get(ID_INDEX)));
            return "Задача с id " + args.get(ID_INDEX) + " успешно удалена.";
        } catch (NoSuchElementException | IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @Override
    public String getTitle() {
        return "delete_task";
    }

    @Override
    public String getManual() {
        return """
                Команда для удаления задачи по id <br> <br>
                Шаблон команды: delete_task, id задачи <br> <br>
                Пример использования: <br>
                http://localhost:8080/cli?command=delete_task, 9
                """;
    }
}
