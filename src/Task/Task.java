package Task;

import java.util.Objects;

public class Task {
    protected int id;
    public String name;
    public String description;
    private TaskStatus taskStatus;

    public Task(String name, String description) {
        this.id = 0;
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

    public Task.TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void setId(int id) {
        this.id = id;
    }
}
