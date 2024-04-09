package convertors;

import task.Task;

public class TaskConvertor {
    public String toString(Task task) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,", task.getId(),task.getTaskType(),
                task.getName(), task.getTaskStatus(), task.getDescription(),
                task.getEpicId(), task.getStartTime(), task.getDuration().toMinutes());
    }

}
