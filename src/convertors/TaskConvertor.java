package convertors;

import task.Task;

public class TaskConvertor {
    public String toString(Task task) {
        return task.getId() + "," + task.getTaskType() + "," + task.getName() +
                "," + task.getTaskStatus() + "," + task.getDescription();
    }

}
