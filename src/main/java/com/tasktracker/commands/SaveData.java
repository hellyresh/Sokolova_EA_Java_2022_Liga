package com.tasktracker.commands;

import com.tasktracker.services.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class SaveData implements Command {

    @Autowired
    private TaskService taskService;

    @Override
    public String execute(List<String> args){
        return taskService.saveData();
    }

    @Override
    public String getTitle() {
        return "save_data";
    }

    @Override
    public String getManual() {
        return """
                Команда для сохранения состояния таск трекера в файлы <br>
                Пример использования: http://localhost:8080/cli?command=save_data
                """;
    }
}
