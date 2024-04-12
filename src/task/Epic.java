package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    public List<Subtask> epicSubtasks;

    public LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        epicSubtasks = new ArrayList<>();
        this.startTime = LocalDateTime.now();
        this.duration = Duration.ofMinutes(0);
        this.endTime = startTime;
    }

    public Epic(int id, String name, String description, TaskStatus status,
                LocalDateTime startTime, Duration duration, LocalDateTime endTime) {
        super(name, description);
        epicSubtasks = new ArrayList<>();
        this.startTime = LocalDateTime.now();
        this.duration = Duration.ofMinutes(0);
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "Эпик №: " + id +
                ", Название: " + name +
                ", Описание: " + description +
                ", Статус: " + taskStatus + ",\n" +
                "Начало: " + startTime +
                ", Длительность: " + duration +
                ", Окончание: " + endTime +
                ".\n";
    }

   public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public TaskType getTaskType() {
        return TaskType.EPIC;
    }

    public void setParameters(Epic epic) {
        List<Subtask> subtasksEpic = epic.epicSubtasks;
        if (subtasksEpic.isEmpty()) {
            epic.setTaskStatus(TaskStatus.NEW);
            epic.endTime = LocalDateTime.now();
            epic.startTime = LocalDateTime.now();
            epic.duration = Duration.ofMinutes(0);
        } else {
            int statusNew = 0;
            int statusDone = 0;
            int statusInProgress = 0;
            LocalDateTime minTime = subtasksEpic.get(0).startTime;
            LocalDateTime maxTime = subtasksEpic.get(0).getEndTime();
            for (Subtask subtask: subtasksEpic) {
                if (minTime.isAfter(subtask.startTime)) {
                    minTime = subtask.startTime;
                }
                if (maxTime.isBefore(subtask.getEndTime())) {
                    maxTime = subtask.getEndTime();
                }
                switch (subtask.getTaskStatus()) {
                    case NEW:
                        statusNew += 1;
                        break;
                    case DONE:
                        statusDone += 1;
                        break;
                    case IN_PROGRESS:
                        statusInProgress += 1;
                        break;
                }
            }
            if (statusDone < 1 && statusInProgress < 1) {
                epic.setTaskStatus(TaskStatus.NEW);
            } else if (statusNew < 1 && statusInProgress < 1) {
                epic.setTaskStatus(TaskStatus.DONE);
            } else {
                epic.setTaskStatus(TaskStatus.IN_PROGRESS);
            }
            epic.startTime = minTime;
            epic.endTime = maxTime;
            epic.duration = Duration.between(minTime, maxTime);
        }
    }

}
