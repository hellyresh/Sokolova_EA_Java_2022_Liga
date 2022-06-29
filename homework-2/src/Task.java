import java.time.LocalDate;

public class Task {

    private final int id;
    private String header;
    private String description;
    private int userId;
    private LocalDate deadline;
    private Status status;

    public Task(int id, String header, String description, int userId, LocalDate deadline) {
        this.id = id;
        this.header = header;
        this.description = description;
        this.userId = userId;
        this.deadline = deadline;
        this.status = Status.NEW;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String toString() {
        return "id задачи  \t" + id + "\n" +
                "заголовок \t" + header + "\n" +
                "описание  \t" + description + "\n" +
                "дедлайн   \t" + deadline + "\n" +
                "статус    \t" + status + "\n";
    }


}
