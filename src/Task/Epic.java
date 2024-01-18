package Task;

import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Subtask> epicSubtasks;
    private TaskStatus taskStatus;

    public Epic(int id, String name, String description) {
        super(name, description);
        this.id = id;
        epicSubtasks = new ArrayList<>();
    }

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
