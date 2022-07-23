package com.tasktracker.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import com.tasktracker.services.TaskService;
import com.tasktracker.services.UserService;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {DeleteAllUsersAndTasks.class})
@ExtendWith(SpringExtension.class)
class DeleteAllUsersAndTasksTest {
    @Autowired
    private DeleteAllUsersAndTasks deleteAllUsersAndTasks;

    @MockBean
    private TaskService taskService;

    @MockBean
    private UserService userService;


    @Test
    void execute_validArgs_deleteMethodsCalled() {
        assertEquals("Данные удалены, чтобы очистить csv файлы не забудьте сохранить изменения.",
                deleteAllUsersAndTasks.execute(new ArrayList<>()));
        verify(taskService).deleteAllTasks();
        verify(userService).deleteAllUsers();
    }


}

