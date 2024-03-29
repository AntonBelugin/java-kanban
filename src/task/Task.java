package task;

import java.util.Objects;

public class Task {
    protected int id;
    protected int epicId;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String name;
    public String description;
    protected TaskStatus taskStatus;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Задача №: " + id +
                " Наименование: " + name +
                ", Описание: " + description +
                ", Статус: " + taskStatus +
                ".";
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
        } else {
            return;
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

}
