package Task;

public class Subtask extends Task {
    public int epicId;
    private TaskStatus taskStatus;
    public Subtask(String name, String description) {
        super(name, description);
    }

    @Override
    public String toString() {
        return "Субзадача №: " + id +
                ", Эпик №: " + epicId +
                ", Название: " + name +
                ", Описание: " + description +
                ", Статус: " + taskStatus +
                '.';
    }

    public Task.TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Task.TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }
}
