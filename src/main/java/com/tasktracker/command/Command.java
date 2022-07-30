package com.tasktracker.command;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface Command {

    Object execute(List<String> args);

    String getTitle();

    String getManual();

}
