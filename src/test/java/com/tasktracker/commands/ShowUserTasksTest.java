package com.tasktracker.commands;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tasktracker.model.Status;
import com.tasktracker.model.Task;
import com.tasktracker.model.User;
import com.tasktracker.services.UserService;

import java.time.LocalDate;
import java.util.*;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ShowUserTasks.class})
@ExtendWith(SpringExtension.class)
class ShowUserTasksTest {
    @Autowired
    private ShowUserTasks showUserTasks;

    @MockBean
    private UserService userService;


    @Test
    @DisplayName("Test not enough arguments in command")
    void execute_notEnoughArgs_exceptionThrown() {
        assertThrows(IndexOutOfBoundsException.class, () -> showUserTasks.execute(new ArrayList<>()));
    }

    @Test
    @DisplayName("Test invalid user id in command")
    void execute_invalidUserId_exceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> showUserTasks.execute(List.of("invalid id")));
    }

    @Test
    @DisplayName("Test invalid status in command")
    void execute_invalidStatus_exceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> showUserTasks.execute(List.of("1", "invalid status")));
    }


    @Test
    @DisplayName("Test valid arguments in command")
    void execute_validArgs_getMethodsCalled() {
        Task task = new Task(1, "h", "d", 1, LocalDate.parse("2022-01-01"), Status.NEW);
        when(userService.getUserById(anyInt())).thenReturn(new User(1, "Name"));
        when(userService.getUserTasksByStatus(any(User.class), any(Status.class)))
                        .thenReturn(Set.of(task));
        showUserTasks.execute(List.of("1", "new"));
        verify(userService).getUserById(1);
        verify(userService).getUserTasksByStatus(any(User.class), any(Status.class));
    }



}

