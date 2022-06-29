import java.util.*;


public class User {

    private final int id;
    private String name;
    private Set<Task> tasks = new HashSet<>();

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public String toString() {
        return id + "\t" + name;
    }
}
