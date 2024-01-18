package Task;

import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Subtask> epicSubtasks;
    private TaskStatus taskStatus;

    public Epic(String name, String description) {
        super(name, description);
        epicSubtasks = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Эпик №: " + id +
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
