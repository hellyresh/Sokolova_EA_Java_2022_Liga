package com.tasktracker.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tasktracker.service.TaskService;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {SaveData.class})
@ExtendWith(SpringExtension.class)
class SaveDataTest {
    @Autowired
    private SaveData saveData;

    @MockBean
    private TaskService taskService;

    @Test
    @DisplayName("Test valid command")
    void execute_validArgs_saveMethodCalled() {
        when(taskService.saveData()).thenReturn("Data saved");
        assertEquals("Data saved", saveData.execute(new ArrayList<>()));
        verify(taskService).saveData();
    }

}

