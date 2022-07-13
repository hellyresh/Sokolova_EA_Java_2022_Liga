package com.tasktracker.commands;

import com.tasktracker.model.Task;
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
public class UpdateTask implements Command {

    private final int TASK_ID_INDEX = 0;
    private final int FIRST_FLAG_INDEX = 1;
    private final int MIN_ARGS_COUNT = 2;
    private final char FLAG_VALUE_DELIMITER = ' ';

    @Autowired
    private TaskService taskService;

    @Override
    public Object execute(List<String> args) {
        if (args.size() < MIN_ARGS_COUNT) {
            return "Некорректное количество аргументов";
        }
        try {
            Task task = taskService.getTaskById(parseInt(args.get(TASK_ID_INDEX)));
            args.subList(FIRST_FLAG_INDEX, args.size())
                    .forEach(field -> update(task, field));
            return task;
        } catch (NumberFormatException e) {
            return "Некорректный ввод команды";
        } catch (NoSuchElementException | IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    private void update(Task task, String field) {

        int spaceInd = field.indexOf(FLAG_VALUE_DELIMITER);
        if (spaceInd == -1) {
            throw new IllegalArgumentException("Некорректный ввод команды, проверьте наличие флагов");
        }
        String flag = field.substring(0, spaceInd);
        String value = field.substring(++spaceInd);
        switch (flag) {
            case "-h" -> taskService.updateTaskHeader(task, value);
            case "-d" -> taskService.updateTaskDescription(task, value);
            case "-u" -> taskService.updateTasksUserId(task, parseInt(value));
            case "-dl" -> taskService.updateTaskDeadline(task, value);
            case "-s" -> taskService.updateTaskStatus(task, value);
            default -> throw new IllegalArgumentException("Некорректный ввод команды, проверьте наличие флагов");
        }
    }

    @Override
    public String getTitle() {
        return "update_task";
    }

    @Override
    public String getManual() {
        return """
                Команда для редактирования задачи <br> <br>
                Аргументы команды отделяются запятой <br> <br>
                Для указания изменяемого поля используются флаги <br> <br>
                Шаблон команды: update_task, id задачи, [-h заголовок], [-d описание задачи], [-u id пользователя], [-dl дедлайн],
                 [-s статус] <br> <br>
                Формат даты: дд.мм.гггг; статус может принимать значения: new, in_process, done <br> <br>
                Аргументы команды могут идти в произвольном порядке и количестве <br> <br>
                Пример использования: <br>
                http://localhost:8080/cli?command=update_task, 1, -h Сделать домашку, -d Написать описание команд в таск
                 трекере, -u 2, -dl 21.03.2030, -s in_process <br>
                http://localhost:8080/cli?command=update_task, 1, -h Отдохнуть, -u 2, -dl 21.03.2030 <br>
                http://localhost:8080/cli?command=update_task, 1, -dl 21.03.2030, -u 2, -h Отдохнуть
                """;
    }
}
