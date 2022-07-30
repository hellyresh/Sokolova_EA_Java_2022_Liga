package com.tasktracker.service;

import com.tasktracker.command.Command;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.Arrays.stream;

@Service
@AllArgsConstructor
public class CommandRouter {

    private final int COMMAND_INDEX = 0;
    private final int FIRST_ARG_INDEX = 1;
    private final String ARGS_DELIMITER = ",";

    @Autowired
    private List<Command> commandList;

    private HashMap<String, Command> commandMap;

    @PostConstruct
    private void registerCommands() {
        for (Command command : commandList) {
            commandMap.put(command.getTitle(), command);
        }
    }

    public Object executeCommand(String command) {
        try {
            List<String> args = stream(command.split(ARGS_DELIMITER)).map(String::trim).toList();
            return commandMap.get(args.get(COMMAND_INDEX).toLowerCase()).execute(args.subList(FIRST_ARG_INDEX, args.size()));
        } catch (NoSuchElementException | IllegalArgumentException | IndexOutOfBoundsException e) {
            return e.getMessage();
        } catch (NullPointerException e) {
            return "Ошибка ввода команды";
        }
    }

}
