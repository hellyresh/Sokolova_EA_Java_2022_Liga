package com.tasktracker.commands;

import com.tasktracker.services.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.Integer.parseInt;

@Component
@AllArgsConstructor
public class CreateTask implements Command {

    @Autowired
    private TaskService taskService;

    @Override
    public Object execute(List<String> args) {
        if (args.size() < 4) {
            return "Некорректное количество аргументов";
        }

        try {
            if (args.size() <= 4)
                return taskService.createTask(args.get(0), args.get(1), parseInt(args.get(2)), args.get(3));

            return taskService.createTask(args.get(0), args.get(1), parseInt(args.get(2)), args.get(3), args.get(4));

        } catch (NoSuchElementException | IllegalArgumentException e) {
            return e.getMessage();
        } catch (DateTimeParseException e) {
            return "Ошибка ввода данных. Введите дату в формате: дд.мм.гггг";
        }
    }

    @Override
    public String getTitle() {
        return "create_task";
    }

    @Override
    public String getManual() {
        return """
                Команда для создания новой задачи <br> <br>
                Аргументы команды отделяются запятой <br> <br>
                Шаблон команды: create_task, заголовок, описание задачи, id пользователя, дедлайн, [статус] <br> <br>
                Формат даты: дд.мм.гггг; статус может принимать значения: new, in_process, done <br> <br>
                Статус - опциональный параметр, если его не указывать задаче автоматически приcвоится статус new <br>
                <br>
                Пример использования: <br>
                http://localhost:8080/cli?command=create_task, Сделать домашку, Написать описание команд в таск трекере,
                 2, 21.03.2030, in_process <br>
                http://localhost:8080/cli?command=create_task, Отдохнуть, Почитать книжку, 2, 21.03.2030
                """;
    }
}