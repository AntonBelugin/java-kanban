package task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    public List<Subtask> epicSubtasks;

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

   public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }
}
