package com.tasktracker.commands;

import com.tasktracker.model.Status;
import com.tasktracker.services.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.Integer.parseInt;

@Component
@AllArgsConstructor
public class CreateTask implements Command {

    private final int HEADER_INDEX = 0;
    private final int DESCRIPTION_INDEX = 1;
    private final int USER_ID_INDEX = 2;
    private final int DEADLINE_INDEX = 3;
    private final int STATUS_INDEX = 4;
    private final int MIN_ARGS_COUNT = 4;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Autowired
    private TaskService taskService;

    @Override
    public Object execute(List<String> args) {
        if (args.size() < MIN_ARGS_COUNT) {
            throw new IndexOutOfBoundsException("Некорректное количество аргументов");
        }

        try {
            if (args.size() == MIN_ARGS_COUNT)
                return taskService.createTask(args.get(HEADER_INDEX),
                        args.get(DESCRIPTION_INDEX),
                        parseInt(args.get(USER_ID_INDEX)),
                        LocalDate.parse(args.get(DEADLINE_INDEX), formatter));

            return taskService.createTask(args.get(HEADER_INDEX),
                    args.get(DESCRIPTION_INDEX),
                    parseInt(args.get(USER_ID_INDEX)),
                    LocalDate.parse(args.get(DEADLINE_INDEX), formatter),
                    Status.valueOf(args.get(STATUS_INDEX).toUpperCase()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ошибка ввода данных. Значение user id должно быть числом");
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Ошибка ввода, введите дату в формате: дд.мм.гггг");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Ошибка ввода, статус может быть: new, in_process, done");
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
