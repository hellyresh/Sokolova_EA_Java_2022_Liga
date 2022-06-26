import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static java.lang.String.format;

public class UserService {

    private final Map<Integer, User> usersById = new HashMap<>();

    public void addUser(User user) {
        usersById.put(user.getId(), user);
    }

    public void registerTask(Task task) {
        getUserById(task.getUserId()).addTask(task);
    }

    public Collection<User> getAllUsers() {
        return usersById.values();
    }

    public User getUserById(int id) {
        User user = usersById.get(id);
        if (user != null) {
            return user;
        }
        throw new NoSuchElementException(format("Пользователя с id = %d не существует", id));
    }
}
