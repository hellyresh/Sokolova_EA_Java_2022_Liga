package com.tasktracker.service;

import com.tasktracker.model.Status;
import com.tasktracker.model.Task;
import com.tasktracker.model.User;
import com.tasktracker.repository.TaskRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {TaskService.class})
@ExtendWith(SpringExtension.class)
class TaskServiceTest {
    @Autowired
    private TaskService taskService;

    @MockBean
    private UserService userService;
    @MockBean
    private TaskRepo taskRepo;


    private final List<Task> taskList = List.of(new Task(0L, "h", "d", new User(0L, "Zero"),
                    LocalDate.parse("2022-01-01"), Status.NEW),
            new Task(1L, "h", "d", new User(1L, "One"),
                    LocalDate.parse("2023-01-01"), Status.NEW),
            new Task(2L, "h", "d", new User(2L, "Two"),
                    LocalDate.parse("2024-01-01"), Status.IN_PROCESS));


    @Test
    @DisplayName("Add valid task")
    void addTask_validTask_saveMethodCalled() {
        taskService.addTask(taskList.get(0));
        verify(taskRepo).save(taskList.get(0));
    }


//    @Test
//    @DisplayName("Test calling task getId() in addTask()")
//    void addTask_validTask_getIdTaskMethodCalled() {
//        Task task = mock(Task.class);
//        taskService.addTask(task);
//        verify(task).getId();
//    }


    @Test
    @DisplayName("Test getTasks() with valid task list")
    void getTasks_validTasks_findMethodCalled() {
        taskService.getTasks();
        verify(taskRepo).findAll();
    }

//    @Test
//    @DisplayName("Test getTasks() with empty task map in task service")
//    void getTasks_emptyTaskMapInTaskService_emptyTaskList() {
//        assertTrue(taskService.getTasks().isEmpty());
//    }



    @Test
    @DisplayName("Test getTasks() with nonexistent task id")
    void getTaskById_nonexistentTaskId_exceptionThrown() {
        assertThrows(NoSuchElementException.class, () -> (taskService).getTaskById(5L));
    }


    @Test
    @DisplayName("Test valid task id in getTaskById()")
    void getTaskById_validTaskId_validTask() {
        when(taskRepo.findById(0L)).thenReturn(Optional.ofNullable(taskList.get(0)));
        Task task = taskService.getTaskById(0L);
        assertEquals(taskList.get(0), task);
    }


    @Test
    @DisplayName("Test updateTaskStatus() with valid status")
    void updateTaskStatus_setValidStatus_validStatus() {
        Task task = taskList.get(0);
        taskService.updateTaskStatus(task, Status.DONE);
        assertEquals(Status.DONE, task.getStatus());
        verify(taskRepo).save(task);
    }

    @Test
    @DisplayName("Test calling setStatus()")
    void updateTaskStatus_setValidStatus_setMethodCalled() {
        Task task = mock(Task.class);
        taskService.updateTaskStatus(task, Status.DONE);
        verify(task).setStatus(Status.DONE);
        verify(taskRepo).save(task);
    }



    @Test
    @DisplayName("Test valid task in createTask() with status")
    void createTask_validArgsWithStatus_validTask() {
        Task task = taskList.get(2);
        when(userService.getUserById(2L)).thenReturn(taskList.get(2).getUser());
        Task createdTask = taskService.createTask(task.getHeader(), task.getDescription(), task.getUserId(),
                task.getDeadLine().format(TaskService.formatter), task.getStatus().toString());
        assertEquals(task.getDeadLine(), createdTask.getDeadLine());
        assertEquals(task.getDescription(), createdTask.getDescription());
        assertEquals(task.getHeader(), createdTask.getHeader());
        assertEquals(task.getUserId(), createdTask.getUserId());
        assertEquals(task.getStatus(), createdTask.getStatus());
        verify(taskRepo).save(createdTask);
    }

    @Test
    @DisplayName("Test valid task in createTask() without status")
    void createTask_validArgsWithoutStatus_validTask() {
        Task task = taskList.get(2);
        when(userService.getUserById(2L)).thenReturn(taskList.get(2).getUser());
        Task createdTask = taskService.createTask(task.getHeader(), task.getDescription(), task.getUserId(),
                task.getDeadLine().format(TaskService.formatter));
        assertEquals(task.getDeadLine(), createdTask.getDeadLine());
        assertEquals(task.getDescription(), createdTask.getDescription());
        assertEquals(task.getHeader(), createdTask.getHeader());
        assertEquals(task.getUserId(), createdTask.getUserId());
        assertEquals(Status.NEW, createdTask.getStatus());
        verify(taskRepo).save(createdTask);
    }

    @Test
    @DisplayName("Test invalid deadline in createTask()")
    void createTask_invalidDeadLine_exceptionThrown() {
        Task task = taskList.get(2);
        when(userService.getUserById(2L)).thenReturn(taskList.get(2).getUser());
        assertThrows(IllegalArgumentException.class, () -> taskService.createTask(task.getHeader(), task.getDescription(), task.getUserId(),
                "invalid deadline"));
    }

    @Test
    @DisplayName("Test invalid status in createTask()")
    void createTask_invalidStatus_exceptionThrown() {
        Task task = taskList.get(2);
        when(userService.getUserById(2L)).thenReturn(taskList.get(2).getUser());
        assertThrows(IllegalArgumentException.class, () -> taskService.createTask(task.getHeader(), task.getDescription(), task.getUserId(),
                task.getDeadLine().format(TaskService.formatter), "invalid status"));
    }

    @Test
    @DisplayName("Test calling userService.getUserById() in createTask()")
    void createTask_validArgs_getUserByIdMethodCalled() {
        Task task = taskList.get(2);
        when(userService.getUserById(2L)).thenReturn(taskList.get(2).getUser());
        Task createdTask = taskService.createTask(task.getHeader(), task.getDescription(), task.getUserId(),
                task.getDeadLine().format(TaskService.formatter));
        verify(userService).getUserById(2L);
        verify(taskRepo).save(createdTask);
    }

    @Test
    @DisplayName("Test nonexistent user id in createTask()")
    void createTask_nonexistentUserId_exceptionThrown() {
        Task task = taskList.get(2);
        when(userService.getUserById(anyLong())).thenThrow(NoSuchElementException.class);
        assertThrows(IllegalArgumentException.class, () -> taskService
                .createTask(task.getHeader(), task.getDescription(), task.getUserId(),
                task.getDeadLine().format(TaskService.formatter), task.getStatus().name()));
    }


    @Test
    @DisplayName("Test updateTaskDeadline() with valid deadline")
    void updateTaskDeadline_setValidDeadline_validDeadline() {
        Task task = taskList.get(0);
        taskService.updateTaskDeadline(task, LocalDate.now());
        assertEquals(LocalDate.now(), task.getDeadLine());
        verify(taskRepo).save(task);
    }

    @Test
    @DisplayName("Test calling setDeadline()")
    void updateTaskDeadline_setValidDeadline_setMethodCalled() {
        Task task = mock(Task.class);
        taskService.updateTaskDeadline(task, LocalDate.parse("2022-12-12"));
        verify(task).setDeadLine(LocalDate.parse("2022-12-12"));
        verify(taskRepo).save(task);
    }


//    @Test
//    @DisplayName("Test updateTasksUserId() with nonexistent user id")
//    void updateTasksUserId_nonexistentUserId_exceptionThrown() {
//        TaskService taskService = new TaskService(new UserService());
//        Task task = taskList.get(0);
//        assertThrows(NoSuchElementException.class, () -> taskService.updateTasksUserId(task, 1L));
//    }

    @Test
    @DisplayName("Test updateTasksUserId() with existent user id")
    void updateTasksUserId_existentUserId_validUserId() {
        User user = new User(5L, "Name");
        when(userService.getUserById(anyLong())).thenReturn(user);
        Task task = taskList.get(0);
        taskService.updateTasksUserId(task, 5L);
        assertEquals(5, task.getUserId());
        verify(taskRepo).save(task);
    }

    @Test
    @DisplayName("Test calling set, get, link and unlink methods with valid user id")
    void updateTasksUserId_existentUserId_methodsCalled() {
        Task task = mock(Task.class);
        taskService.updateTasksUserId(task, 5L);
        verify(task).setUser(any());
        verify(userService).getUserById(anyLong());
        verify(taskRepo).save(task);
    }

    @Test
    @DisplayName("Test updateTaskDeadline() with valid deadline")
    void updateTaskDescription_setValidDescription_validDescription() {
        Task task = taskList.get(0);
        taskService.updateTaskDescription(task, "Description");
        assertEquals("Description", task.getDescription());
        verify(taskRepo).save(task);
    }

    @Test
    @DisplayName("Test calling setDescription()")
    void updateTaskDescription_setValidDescription_setMethodCalled() {
        Task task = mock(Task.class);
        taskService.updateTaskDescription(task, "Description");
        verify(task).setDescription("Description");
        verify(taskRepo).save(task);
    }

    @Test
    @DisplayName("Test updateTaskHeader() with valid header")
    void updateTaskHeader_setValidHeader_validHeader() {
        Task task = taskList.get(0);
        taskService.updateTaskHeader(task, "Header");
        assertEquals("Header", task.getHeader());
        verify(taskRepo).save(task);
    }

    @Test
    @DisplayName("Test calling setDescription()")
    void updateTaskHeader_setValidHeader_setMethodCalled() {
        Task task = mock(Task.class);
        taskService.updateTaskHeader(task, "Header");
        verify(task).setHeader("Header");
        verify(taskRepo).save(task);
    }


    @Test
    @DisplayName("Test deleteTaskById() with nonexistent task id")
    void deleteTaskById_nonexistentTaskId_exceptionThrown() {
        assertThrows(NoSuchElementException.class, () -> (taskService).deleteTaskById(1L));
    }

    @Test
    @DisplayName("Test deleteTaskById() with existent task id")
    void deleteTaskById_existentTaskId_deleteMethodCalled() {
        Task task = taskList.get(0);
        when(taskRepo.findById(any())).thenReturn(Optional.ofNullable(task));
        assert task != null;
        taskService.deleteTaskById(task.getId());
        verify(taskRepo).delete(task);
    }


    @Test
    @DisplayName("Test valid deleteAllTasks()")
    void deleteAllTasks_deleteMethodCalled() {
        taskService.deleteAllTasks();
        verify(taskRepo).deleteAll();
    }


    @Test
    @DisplayName("Test valid saveData()")
    void saveData_dataSavedMessage() {
        assertEquals("Данные успешно сохранены", taskService.saveData());
    }


}

