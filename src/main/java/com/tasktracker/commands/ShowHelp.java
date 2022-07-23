package com.tasktracker.commands;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.joining;

@Component
@AllArgsConstructor
public class ShowHelp implements Command {

    private final int COMMAND_INDEX = 0;

    @Autowired
    private List<Command> commandList;

    private final static String intro = """
            Для работы с таск трекером введите в адресной строке:</br>
            http://localhost:8080/cli?command=название_команды, [param[, param, [param, ...]]]</br></br>
            Для получения справки по команде, перейдите по адресу:</br>
            http://localhost:8080/cli?command=help, [название_команды]</br></br>
            Список доступных команд:</br>
            """;

    @Override
    public Object execute(List<String> args) {
        if (args.isEmpty()) {
            return commandList.stream()
                    .map(Command::getTitle)
                    .collect(joining("</br>", intro, ""));
        }

        String commandTitle = args.get(COMMAND_INDEX);
        for (Command command : commandList) {
            if (command.getTitle().equals(commandTitle.toLowerCase())) {
                return command.getManual();
            }
        }

        return "Нет команды с названием " + commandTitle;
    }

    @Override
    public String getTitle() {
        return "help";
    }

    @Override
    public String getManual() {
        return "";
    }
}
