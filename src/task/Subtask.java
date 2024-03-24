package task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(Integer epicId, String name, String description, String startTime, int duration) {
        super(name, description);
        this.epicId = epicId;
        this.startTime = LocalDateTime.parse(startTime, inputFormatter);
        this.duration = Duration.ofMinutes(duration);
    }

    public Subtask(Integer epicId, String name, String description) {
        super(name, description);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Субзадача №: " + id +
                ", Эпик №: " + epicId +
                ", Название: " + name +
                ", Описание: " + description +
                ", Статус: " + taskStatus + ",\n" +
                "Начало: " + startTime +
                ", Длительность: " + duration +
                ", Окончание: " + getEndTime() +
                ".\n";
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public TaskType getTaskType() {
        return TaskType.SUBTASK;
    }
}


