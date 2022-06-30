package hellyresh.model;

import java.util.HashSet;
import java.util.Set;

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

    public String toString() {
        return id + "\t" + name;
    }

    public String toCsvRow() {
        return id + ", " + name + "\n";
    }

}
