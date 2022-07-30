package com.tasktracker.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tasktracker.model.Status;
import com.tasktracker.model.Task;
import com.tasktracker.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {TaskService.class})
@ExtendWith(SpringExtension.class)
class TaskServiceTest {
    @Autowired
    private TaskService taskService;

    @MockBean
    private UserService userService;

    @BeforeEach
    void clearTaskService(){
        taskService.deleteAllTasks();
    }

    private final List<Task> taskList = List.of(new Task(0, "h", "d", 1,
                    LocalDate.parse("2022-01-01"), Status.NEW),
            new Task(1, "h", "d", 1,
                    LocalDate.parse("2023-01-01"), Status.NEW),
            new Task(2, "h", "d", 1,
                    LocalDate.parse("2024-01-01"), Status.IN_PROCESS));


    @Test
    @DisplayName("Add valid task")
    void addTask_validTask_taskAddedAndLinkMethodCalled() {
        assertTrue(taskService.getTasks().isEmpty());
        taskService.addTask(taskList.get(0));
        verify(userService).linkTask(any());
        assertEquals(1, taskService.getTasks().size());
        assertEquals(taskList.get(0), taskService.getTaskById(taskList.get(0).getId()));
    }


    @Test
    @DisplayName("Test calling task getId() in addTask()")
    void addTask_validTask_getIdTaskMethodCalled() {
        Task task = mock(Task.class);
        taskService.addTask(task);
        verify(task).getId();
    }


    @Test
    @DisplayName("Test getTasks() with valid task list")
    void getTasks_addedTasks_taskList() {
        for (Task task : taskList) {
            taskService.addTask(task);
        }
        assertEquals(taskList, taskService.getTasks().stream().toList());
    }

    @Test
    @DisplayName("Test getTasks() with empty task map in task service")
    void getTasks_emptyTaskMapInTaskService_emptyTaskList() {
        assertTrue(taskService.getTasks().isEmpty());
    }



    @Test
    @DisplayName("Test getTasks() with nonexistent task id")
    void getTaskById_nonexistentTaskId_exceptionThrown() {
        assertThrows(NoSuchElementException.class, () -> (taskService).getTaskById(5));
    }


    @Test
    @DisplayName("Test valid task id in getTaskById()")
    void getTaskById_validTaskId_validTask() {
        for (Task task : taskList) {
            taskService.addTask(task);
        }
        assertEquals(taskList.get(0), taskService.getTaskById(0));
    }


    @Test
    @DisplayName("Test updateTaskStatus() with valid status")
    void updateTaskStatus_setValidStatus_validStatus() {
        Task task = taskList.get(0);
        taskService.updateTaskStatus(task, Status.DONE);
        assertEquals(Status.DONE, task.getStatus());
    }

    @Test
    @DisplayName("Test calling setStatus()")
    void updateTaskStatus_setValidStatus_setMethodCalled() {
        Task task = mock(Task.class);
        taskService.updateTaskStatus(task, Status.DONE);
        verify(task).setStatus(any());
    }



    @Test
    @DisplayName("Test valid task in createTask() with status")
    void createTask_validArgsWithStatus_validTask() {
        Task task = taskList.get(2);
        Task createdTask = taskService.createTask(task.getHeader(), task.getDescription(), task.getUserId(),
                task.getDeadLine(), task.getStatus());
        assertEquals(task.getDeadLine(), createdTask.getDeadLine());
        assertEquals(task.getDescription(), createdTask.getDescription());
        assertEquals(task.getHeader(), createdTask.getHeader());
        assertEquals(task.getUserId(), createdTask.getUserId());
        assertEquals(task.getStatus(), createdTask.getStatus());
    }

    @Test
    @DisplayName("Test valid task in createTask() without status")
    void createTask_validArgsWithoutStatus_validTask() {
        Task task = taskList.get(2);
        Task createdTask = taskService.createTask(task.getHeader(), task.getDescription(), task.getUserId(),
                task.getDeadLine());
        assertEquals(task.getDeadLine(), createdTask.getDeadLine());
        assertEquals(task.getDescription(), createdTask.getDescription());
        assertEquals(task.getHeader(), createdTask.getHeader());
        assertEquals(task.getUserId(), createdTask.getUserId());
        assertEquals(Status.NEW, createdTask.getStatus());
    }

    @Test
    @DisplayName("Test calling userService.getUserById() in createTask()")
    void createTask_validArgs_getUserByIdMethodCalled() {
        Task task = taskList.get(2);
        taskService.createTask(task.getHeader(), task.getDescription(), task.getUserId(),
                task.getDeadLine());
        verify(userService).getUserById(anyInt());
    }

    @Test
    @DisplayName("Test nonexistent user id in createTask()")
    void createTask_nonexistentUserId_exceptionThrown() {
        TaskService taskService = new TaskService(new UserService());
        Task task = taskList.get(2);
        assertThrows(NoSuchElementException.class, () -> taskService.createTask(task.getHeader(), task.getDescription(), task.getUserId(),
                task.getDeadLine()));
    }


    @Test
    @DisplayName("Test updateTaskDeadline() with valid deadline")
    void updateTaskDeadline_setValidDeadline_validDeadline() {
        Task task = taskList.get(0);
        taskService.updateTaskDeadline(task, LocalDate.now());
        assertEquals(LocalDate.now(), task.getDeadLine());
    }

    @Test
    @DisplayName("Test calling setDeadline()")
    void updateTaskDeadline_setValidDeadline_setMethodCalled() {
        Task task = mock(Task.class);
        taskService.updateTaskDeadline(task, LocalDate.now());
        verify(task).setDeadLine(any());
    }

    @Test
    @DisplayName("Test updateTasksUserId() with nonexistent user id")
    void updateTasksUserId_nonexistentUserId_exceptionThrown() {
        TaskService taskService = new TaskService(new UserService());
        Task task = taskList.get(0);
        assertThrows(NoSuchElementException.class, () -> taskService.updateTasksUserId(task, 1));
    }

    @Test
    @DisplayName("Test updateTasksUserId() with existent user id")
    void updateTasksUserId_existentUserId_validUserId() {
        when(userService.getUserById(anyInt())).thenReturn(new User(5, "Name"));
        Task task = taskList.get(0);
        taskService.updateTasksUserId(task, 5);
        assertEquals(5, task.getUserId());
    }

    @Test
    @DisplayName("Test calling set, get, link and unlink methods with valid user id")
    void updateTasksUserId_existentUserId_methodsCalled() {
        Task task = mock(Task.class);
        taskService.updateTasksUserId(task, 5);
        verify(task).setUserId(anyInt());
        verify(userService).getUserById(anyInt());
        verify(userService).linkTask(any());
        verify(userService).unlinkTask(any());
    }

    @Test
    @DisplayName("Test updateTaskDeadline() with valid deadline")
    void updateTaskDescription_setValidDescription_validDescription() {
        Task task = taskList.get(0);
        taskService.updateTaskDescription(task, "Description");
        assertEquals("Description", task.getDescription());
    }

    @Test
    @DisplayName("Test calling setDescription()")
    void updateTaskDescription_setValidDescription_setMethodCalled() {
        Task task = mock(Task.class);
        taskService.updateTaskDescription(task, "Description");
        verify(task).setDescription(any());
    }

    @Test
    @DisplayName("Test updateTaskHeader() with valid header")
    void updateTaskHeader_setValidHeader_validHeader() {
        Task task = taskList.get(0);
        taskService.updateTaskHeader(task, "Header");
        assertEquals("Header", task.getHeader());
    }

    @Test
    @DisplayName("Test calling setDescription()")
    void updateTaskHeader_setValidHeader_setMethodCalled() {
        Task task = mock(Task.class);
        taskService.updateTaskHeader(task, "Header");
        verify(task).setHeader(any());
    }


    @Test
    @DisplayName("Test deleteTaskById() with nonexistent task id")
    void deleteTaskById_nonexistentTaskId_exceptionThrown() {
        assertThrows(NoSuchElementException.class, () -> (taskService).deleteTaskById(1));
    }

    @Test
    @DisplayName("Test deleteTaskById() with existent task id")
    void deleteTaskById_existentTaskId_taskDeleted() {
        Task task = taskList.get(0);
        taskService.addTask(task);
        assertFalse(taskService.getTasks().isEmpty());
        taskService.deleteTaskById(task.getId());
        assertTrue(taskService.getTasks().isEmpty());
        verify(userService).linkTask(any());
        verify(userService).unlinkTask(any());
    }


    @Test
    @DisplayName("Test valid deleteAllTasks()")
    void deleteAllTasks_taskList_tasksDeleted() {
        for (Task task : taskList) {
            taskService.addTask(task);
        }
        assertFalse(taskService.getTasks().isEmpty());
        taskService.deleteAllTasks();
        assertTrue(taskService.getTasks().isEmpty());
    }


    @Test
    @DisplayName("Test valid saveData()")
    void saveData() {
        assertEquals("Данные успешно сохранены", taskService.saveData());
    }


}

