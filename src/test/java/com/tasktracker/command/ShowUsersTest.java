package com.tasktracker.command;

import static org.mockito.Mockito.*;

import com.tasktracker.service.UserService;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ShowUsers.class})
@ExtendWith(SpringExtension.class)
class ShowUsersTest {
    @Autowired
    private ShowUsers showUsers;

    @MockBean
    private UserService userService;


    @Test
    @DisplayName("Test valid command")
    void execute_validArgs_getMethodCalled() {
        showUsers.execute(new ArrayList<>());
        verify(userService).getUsers();
    }
}

