package com.tasktracker.services;

import com.tasktracker.commands.Command;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.stream;

@Service
@AllArgsConstructor
public class CommandRouter {

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
        List<String> args = stream(command.split(",")).map(String::trim).toList();
        return commandMap.get(args.get(0).toLowerCase()).execute(args.subList(1, args.size()));
    }

}
