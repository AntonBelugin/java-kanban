package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Subtask extends Task {

    public Subtask(Integer epicId, String name, String description, String startTime, long duration) {
        super(name, description);
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        this.epicId = epicId;
        this.startTime = LocalDateTime.parse(startTime, inputFormatter);
        this.duration = Duration.ofMinutes(duration);
    }

    public Subtask(Integer id, Integer epicId, String name, String description, String startTime, long duration) {
        super(name, description);
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        this.id = id;
        this.epicId = epicId;
        this.startTime = LocalDateTime.parse(startTime, inputFormatter);
        this.duration = Duration.ofMinutes(duration);
    }

    public Subtask(Integer id, Integer epicId, String name, String description, TaskStatus taskStatus, String startTime, long duration) {
        super(name, description);
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        this.id = id;
        this.epicId = epicId;
        this.taskStatus = taskStatus;
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


