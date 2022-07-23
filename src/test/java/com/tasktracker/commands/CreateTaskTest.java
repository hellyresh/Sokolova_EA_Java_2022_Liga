package com.tasktracker.commands;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

import com.tasktracker.services.TaskService;
import com.tasktracker.services.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CreateTask.class})
@ExtendWith(SpringExtension.class)
class CreateTaskTest {
    @Autowired
    private CreateTask createTask;

    @MockBean
    private TaskService taskService;

    @MockBean
    private UserService userService;


    @Test
    @DisplayName("Test not enough arguments in command")
    void execute_notEnoughArgs_exceptionThrown() {
        assertThrows(IndexOutOfBoundsException.class, () -> createTask.execute(new ArrayList<>()));
    }

    @Test
    @DisplayName("Test invalid deadline in command")
    void execute_invalidDeadline_exceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> createTask
                .execute(List.of("Header", "Description", "2", "invalid deadline", "in_process")));
    }

    @Test
    @DisplayName("Test invalid user id in command")
    void execute_invalidUserId_exceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> createTask
                .execute(List.of("Header", "Description", "invalid id", "21.01.2023", "in_process")));
    }

    @Test
    @DisplayName("Test invalid status in command")
    void execute_invalidStatus_exceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> createTask
                .execute(List.of("Header", "Description", "1", "21.01.2023", "invalid status")));
    }

    @Test
    @DisplayName("Test valid arguments in command")
    void execute_validArgs_createTaskMethodCalled() {
        createTask.execute(List.of("Header", "Description", "1", "01.01.2023"));
        verify(taskService)
                .createTask(anyString(), anyString(), anyInt(), any(LocalDate.class));
    }

}

