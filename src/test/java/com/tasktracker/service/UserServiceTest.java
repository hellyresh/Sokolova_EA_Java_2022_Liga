package com.tasktracker.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.tasktracker.model.Status;
import com.tasktracker.model.Task;
import com.tasktracker.model.User;

import java.time.LocalDate;

import java.util.*;


import com.tasktracker.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserService.class})
@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepo userRepo;


    private final List<User> userList = List.of(new User(0L, "First"),
            new User(1L, "Second"), new User(2L, "Third"));

//    private final List<Task> taskList = List.of(new Task(0L, "h", "d", new User(),
//                    LocalDate.parse("2022-01-01"), Status.NEW),
//            new Task(1L, "h", "d", new User(),
//                    LocalDate.parse("2023-01-01"), Status.NEW),
//            new Task(2L, "h", "d", new User(),
//                    LocalDate.parse("2024-01-01"), Status.IN_PROCESS));


    @Test
    @DisplayName("Add valid user")
    void addTask_validUser_saveMethodCalled() {
        userService.addUser(userList.get(0));
        verify(userRepo).save(userList.get(0));
    }


//    @Test
//    @DisplayName("Link task to nonexistent user")
//    void linkTask_nonexistentUserId_exceptionThrown() {
//        Task task = taskList.get(0);
//        assertThrows(NoSuchElementException.class, () -> userService.linkTask(task));
//    }

//    @Test
//    @DisplayName("Link task to existent user")
//    void linkTask_existentUserId_linkedTask() {
//        Task task = taskList.get(0);
//        userService.addUser(userList.get(1));
//        userService.linkTask(task);
//        assertTrue(userService.getUserById(userList.get(1).getId()).getTasks().contains(task));
//    }


//    @Test
//    @DisplayName("Test getUsers() with empty user map in user service")
//    void getUsers_emptyUserMapInUserService_emptyUserList() {
//        assertTrue(userService.getUsers().isEmpty());
//    }

    @Test
    @DisplayName("Test getUsers() with valid user list")
    void getUsers_userList() {
        userService.getUsers();
        verify(userRepo).findAll();
    }


    @Test
    @DisplayName("Get user by nonexistent id")
    void getUserById_nonexistentUserId_exceptionThrown() {
        assertThrows(NoSuchElementException.class, () -> userService.getUserById(1L));
    }


//    @Test
//    @DisplayName("Get user tasks when user has no tasks")
//    void getUserTasksByStatus_noUserTasks_emptyTaskList() {
//        User user = userList.get(1);
//        assertTrue(userService.getUserTasksByStatus(user, Status.NEW).isEmpty());
//    }

//    @Test
//    @DisplayName("Get user tasks by valid status")
//    void getUserTasksByStatus_statusNew_filteredTaskList() {
//        User user = userList.get(1);
//        userService.addUser(user);
//        List<Task> taskListNew = new ArrayList<>();
//        for (Task task : taskList) {
//            //userService.linkTask(task);
//            if (task.getStatus().equals(Status.NEW)) {
//                taskListNew.add(task);
//            }
//        }
//        assertEquals(new HashSet<>(taskListNew), userService.getUserTasksByStatus(user, Status.NEW));
//    }

    @Test
    @DisplayName("Test calling getTasks()")
    void getUserTasksByStatus_getTasksMethodCalled() {
        User user = mock(User.class);
        userService.getUserTasksByStatus(user, Status.NEW);
        verify(user).getTasks();
    }


//    @Test
//    @DisplayName("Unlink nonexistent task")
//    void unlinkTask_nonexistentTask_exceptionThrown() {
//        assertThrows(NoSuchElementException.class, () -> userService.unlinkTask(taskList.get(0)));
//    }

//    @Test
//    @DisplayName("Unlink existent task")
//    void unlinkTask_existentTask_taskUnlinked() {
//        User user = userList.get(1);
//        userService.addUser(user);
//        userService.linkTask(taskList.get(0));
//        userService.linkTask(taskList.get(1));
//        userService.linkTask(taskList.get(2));
//        assertEquals(new HashSet<>(taskList), user.getTasks());
//        Task task = taskList.get(0);
//        userService.unlinkTask(task);
//        assertEquals(Set.of(taskList.get(1), taskList.get(2)), user.getTasks());
//    }


    @Test
    @DisplayName("Delete all users in userService")
    void deleteAllUsers_deleteMethodCalled() {
        userService.deleteAllUsers();
        verify(userRepo).deleteAll();
    }
}

