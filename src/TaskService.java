import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static java.lang.String.format;

public class TaskService {

    private final UserService userService;

    private final Map<Integer, Task> tasksById = new HashMap<>();

    public TaskService(UserService userService) {
        this.userService = userService;
    }

    public void addTask(Task task) {
        userService.registerTask(task);
        tasksById.put(task.getId(), task);
    }

    public Collection<Task> getAllTasks() {
        return tasksById.values();
    }

    public Task getTaskById(int id) {
        Task task = tasksById.get(id);
        if (task != null) {
            return task;
        }
        throw new NoSuchElementException(format("Задачи с id = %d не существует", id));
    }

    public void updateTaskStatus(int id, Status status) {
        try {
            getTaskById(id).setStatus(status);
        }
        catch (NoSuchElementException e) {
            throw e;
        }
    }
}
