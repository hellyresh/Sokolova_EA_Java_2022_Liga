package com.tasktracker.commands;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.tasktracker.model.Status;
import com.tasktracker.model.Task;
import com.tasktracker.services.TaskService;

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

@ContextConfiguration(classes = {UpdateTask.class})
@ExtendWith(SpringExtension.class)
class UpdateTaskTest {
    @MockBean
    private TaskService taskService;

    @Autowired
    private UpdateTask updateTask;

    private final Task task = new Task(1, "h", "d", 1,
            LocalDate.parse("2022-01-01"), Status.NEW);


    @Test
    @DisplayName("Test not enough arguments in command")
    void execute_notEnoughArgs_exceptionThrown() {
        assertThrows(IndexOutOfBoundsException.class, () -> updateTask.execute(new ArrayList<>()));
    }


    @Test
    @DisplayName("Test invalid flag in update method")
    void update_invalidFlag_exceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> updateTask.update(task, "-f invalid flag"));
    }

    @Test
    @DisplayName("Test invalid deadline in update method")
    void update_invalidDeadline_exceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> updateTask.update(task, "-dl invalid deadline"));
    }

    @Test
    @DisplayName("Test invalid user id in update method")
    void update_invalidUserId_exceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> updateTask.update(task, "-u invalid id"));
    }

    @Test
    @DisplayName("Test invalid status in update method")
    void update_invalidStatus_exceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> updateTask.update(task, "-s invalid status"));
    }

    @Test
    @DisplayName("Test no flag in update method")
    void update_noFlag_exceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> updateTask.update(task, "no flag"));
    }

    @Test
    @DisplayName("Test valid arguments in command")
    void execute_validArgs_updateMethodsCalled() {
        when(taskService.getTaskById(anyInt())).thenReturn(task);
        doNothing().when(taskService).updateTaskDeadline(any(Task.class), any(LocalDate.class));
        doNothing().when(taskService).updateTaskStatus(any(Task.class), any(Status.class));
        doNothing().when(taskService).updateTaskDescription(any(Task.class), anyString());
        doNothing().when(taskService).updateTaskHeader(any(Task.class), anyString());
        doNothing().when(taskService).updateTasksUserId(any(Task.class), anyInt());
        updateTask.execute(List.of("1", "-h Header", "-d Description", "-u 1", "-dl 21.01.2023", "-s in_process"));
        verify(taskService).getTaskById(1);
        verify(taskService).updateTaskDeadline(any(Task.class), any(LocalDate.class));
        verify(taskService).updateTaskStatus(any(Task.class), any(Status.class));
        verify(taskService).updateTaskDescription(any(Task.class), anyString());
        verify(taskService).updateTaskHeader(any(Task.class), anyString());
        verify(taskService).updateTasksUserId(any(Task.class), anyInt());
    }

}

