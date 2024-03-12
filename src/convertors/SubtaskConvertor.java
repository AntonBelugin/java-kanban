package convertors;

import task.Subtask;
import task.Task;

public class SubtaskConvertor {

    public String toString(Task subtask) {
        return subtask.getId() + "," + subtask.getTaskType() + "," + subtask.getName() +
                "," + subtask.getTaskStatus() + "," + subtask.getDescription() + "," +
                subtask.getEpicId();
    }
}
