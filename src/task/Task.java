package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task implements Comparable<Task> {
    protected int id;
    protected int epicId;
    public String name;
    public String description;
    protected LocalDateTime startTime;
    protected Duration duration;
    protected TaskStatus taskStatus;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task(String name, String description, String startTime, int duration) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        this.name = name;
        this.description = description;
        this.startTime = LocalDateTime.parse(startTime, inputFormatter);
        this.duration = Duration.ofMinutes(duration);
    }

    public Task(int id, String name, String description, String startTime, int duration) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        this.id = id;
        this.name = name;
        this.description = description;
        this.startTime = LocalDateTime.parse(startTime, inputFormatter);
        this.duration = Duration.ofMinutes(duration);
    }

    public Task(int id, String name, String description, TaskStatus taskStatus, String startTime, int duration) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        this.id = id;
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.startTime = LocalDateTime.parse(startTime, inputFormatter);
        this.duration = Duration.ofMinutes(duration);
    }

    @Override
    public String toString() {
        return "Задача №: " + id +
                " Наименование: " + name +
                ", Описание: " + description +
                ", Статус: " + taskStatus + ",\n" +
                "Начало: " + startTime +
                ", Длительность: " + duration +
                ", Окончание: " + getEndTime() +
                ".\n";
    }

    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void setId(int id) {
        if (this.id == 0) {
            this.id = id;
        }
    }

    public TaskType getTaskType() {
        return TaskType.TASK;
    }

   public Integer getEpicId() {
        if (getTaskType() == TaskType.SUBTASK) {
            return epicId;
        } else {
            return 0;
        }
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int compareTo(Task o) {
        return startTime.compareTo(o.startTime);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
