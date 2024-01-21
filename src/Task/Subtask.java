package Task;

public class Subtask extends Task {
    private final Epic epic;
    private TaskStatus taskStatus;

    public Subtask(Epic epic, String name, String description) {
        super(name, description);
        this.epic = epic;
    }

    @Override
    public String toString() {
        return "Субзадача №: " + id +
                ", Эпик №: " + epic.id +
                ", Название: " + name +
                ", Описание: " + description +
                ", Статус: " + taskStatus +
                '.';
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Epic getEpic() {
        return epic;
    }
}
