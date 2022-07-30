package com.tasktracker.commands;

import com.tasktracker.services.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
@AllArgsConstructor
public class LoadDataFromCsv implements Command {
    @Autowired
    private TaskService taskService;

    @Override
    public String execute(List<String> args) {
        return taskService.loadData();
    }

    @Override
    public String getTitle() {
        return "load_data";
    }

    @Override
    public String getManual() {
        return """
                Команда для загрузки состояния таск трекера из файлов <br>
                Пример использования: http://localhost:8080/cli?command=load_data
                """;
    }
}
