package com.tasktracker.command;

import com.tasktracker.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

@Component
@AllArgsConstructor
public class CreateTask implements Command {

    private final int HEADER_INDEX = 0;
    private final int DESCRIPTION_INDEX = 1;
    private final int USER_ID_INDEX = 2;
    private final int DEADLINE_INDEX = 3;
    private final int STATUS_INDEX = 4;
    private final int MIN_ARGS_COUNT = 4;

    @Autowired
    private TaskService taskService;

    @Override
    public Object execute(List<String> args) {
        if (args.size() < MIN_ARGS_COUNT) {
            return "Некорректное количество аргументов";
        }

        try {
            if (args.size() == MIN_ARGS_COUNT)
                return taskService.createTask(args.get(HEADER_INDEX),
                        args.get(DESCRIPTION_INDEX),
                        parseLong(args.get(USER_ID_INDEX)),
                        args.get(DEADLINE_INDEX));

            return taskService.createTask(args.get(HEADER_INDEX),
                    args.get(DESCRIPTION_INDEX),
                    parseLong(args.get(USER_ID_INDEX)),
                    args.get(DEADLINE_INDEX),
                    args.get(STATUS_INDEX));

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
