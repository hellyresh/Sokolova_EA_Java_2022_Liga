package com.tasktracker.commands;


import com.tasktracker.services.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ContextConfiguration(classes = {DeleteTask.class})
@ExtendWith(SpringExtension.class)
class DeleteTaskTest {

    @Autowired
    DeleteTask deleteTask;

    @MockBean
    private TaskService taskService;


    @Test
    @DisplayName("Test not enough arguments in command")
    void execute_notEnoughArgs_exceptionThrown() {
        assertThrows(IndexOutOfBoundsException.class, () -> deleteTask.execute(new ArrayList<>()));
    }

    @Test
    @DisplayName("Test invalid task id in command")
    void execute_invalidTaskId_exceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> deleteTask.execute(List.of("invalid id")));
    }

    @Test
    @DisplayName("Test valid arguments in command")
    void execute_validArgs_createTaskMethodCalled() {
        deleteTask.execute(List.of("1"));
        verify(taskService).deleteTaskById(1);
    }

}

